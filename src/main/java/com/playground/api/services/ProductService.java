package com.playground.api.services;

import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.*;
import com.playground.api.enums.CurrencyType;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import com.playground.api.integrations.ports.MultimediaStorageService;
import com.playground.api.entities.Multimedia;
import com.playground.api.entities.Product;
import com.playground.api.repositories.MultimediaRepository;
import com.playground.api.repositories.ProductRepository;
import com.playground.api.repositories.specifications.ProductSpecifications;
import com.playground.api.utils.CurrencyUtils;
import com.playground.api.utils.MultimediaUtils;
import com.playground.api.utils.PaginationUtils;
import com.playground.api.utils.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MultimediaRepository multimediaRepository;
    private final MultimediaStorageService multimediaStorageService;
    private final MultimediaUtils multimediaUtils;
    private final CurrencyUtils currencyUtils;

    @Autowired
    public ProductService(
            final ProductRepository productRepository,
            final MultimediaRepository multimediaRepository,
            final MultimediaStorageService multimediaStorageService,
            final MultimediaUtils multimediaUtils,
            final CurrencyUtils currencyUtils
    ) {
        this.productRepository = productRepository;
        this.multimediaRepository = multimediaRepository;
        this.multimediaStorageService = multimediaStorageService;
        this.multimediaUtils = multimediaUtils;
        this.currencyUtils = currencyUtils;
    }

    public CreateProductResponse createProduct(CreateProductBody request) {
        // Attempt to find a product with the same name
        Optional<Product> existingProduct = Optional.ofNullable(productRepository.findByNameIgnoreCase(request.getName()));

        // If a product with the same name exists, throw an exception
        if (existingProduct.isPresent() && !existingProduct.get().isDeleted()) {
            throw new ApiException("A product with the same name already exists", ErrorCode.ITEM_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        // Create a new product entity
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());

        // Save the product to the database
        product = productRepository.save(product);

        // Return the created product
        return CreateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(
                        product.getMultimedia() != null
                                ? multimediaStorageService.generatePublicUrl(product.getMultimedia().getUri())
                                : null
                )
                .build();
    }

    public PaginationResponse<ListProductsResponse> listProducts(
            ListProductsQuery request
    ) {
        // Create a specification for filtering products
        Specification<Product> specification = Specification.where(null);

        // Add filters to the specification based on the request
        specification = specification
                .and(SpecificationUtils.optional(request.getName(), ProductSpecifications::nameContains))
                .and(SpecificationUtils.optional(request.getCategory(), ProductSpecifications::inCategory))
                .and(SpecificationUtils.optional(request.getHasStock(), ProductSpecifications::hasStock))
                .and(SpecificationUtils.optional(request.getMinPrice(), ProductSpecifications::priceAtLeast))
                .and(SpecificationUtils.optional(request.getMaxPrice(), ProductSpecifications::priceAtMost))
                .and(ProductSpecifications.notDeleted());

        // Fetch all the products from the repository with given filters
        Page<Product> page = productRepository.findAll(
                specification,
                PaginationUtils.getPaginationFilters(request)
        );

        // Map the products to the response DTO
        List<ListProductsResponse> response = page.getContent().stream().map(
                product -> ListProductsResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .category(product.getCategory())
                        .stockQuantity(product.getStockQuantity())
                        .build()
        ).toList();

        // Create pagination response and return it
        return PaginationUtils.getPaginationResponse(page, response);
    }

    public FindProductResponse findProductById(UUID id) {
        // Attempt to find a product with the given ID
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)
        );

        // If the product is marked as deleted, it does not exist anymore
        if (product.isDeleted()) {
            throw new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

        // Obtain the product's price in different currencies
        Map<CurrencyType, BigDecimal> currencyPrices = currencyUtils
                .getPricesForCurrencies(CurrencyType.USD, BigDecimal.valueOf(product.getPrice()));

        // Return the product information
        return FindProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(
                        product.getMultimedia() != null
                                ? multimediaStorageService.generatePublicUrl(product.getMultimedia().getUri())
                                : null
                )
                .price(currencyPrices)
                .build();
    }

    public UploadProductImageResponse uploadProductImage(UUID productId, MultipartFile file) {
        // Verify that the file is not empty
        if (file.isEmpty()) {
            throw new ApiException("The uploaded file is empty", ErrorCode.FILE_CONTENT_ERROR, HttpStatus.BAD_REQUEST);
        }

        // Validate the file and extract the extension
        String fileExtension = multimediaUtils.validateAndExtractFileExtension(file);

        // Generate the filename (product's ID with extension)
        String fileName = String.format("products/%s.%s", productId, fileExtension);

        // Verify that the product by given ID exists
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)
        );

        // If the product is marked as deleted, it does not exist anymore
        if (product.isDeleted()) {
            throw new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

        // Upload the file into the multimedia storage service
        String uri = multimediaStorageService.upload(file, fileName);

        // Create a multimedia object with the uploaded file's URI
        Multimedia multimedia = new Multimedia();
        multimedia.setUri(uri);
        multimedia.setMimeType(file.getContentType());
        multimedia.setFileName(fileName);
        multimediaRepository.save(multimedia);

        // Associate the multimedia object with the product
        product.setMultimedia(multimedia);
        productRepository.save(product);

        // Return the new URL for the product's image
        return UploadProductImageResponse.builder()
                .url(multimediaStorageService.generatePublicUrl(uri))
                .productId(product.getId())
                .build();
    }

    public UpdateProductResponse updateProduct(UUID id, UpdateProductBody request) {
        // Attempt to find a product with the given ID
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)
        );

        // If the product is marked as deleted, it does not exist anymore
        if (product.isDeleted()) {
            throw new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

        // If the product name is being changed, check for duplicates
        if (!product.getName().equalsIgnoreCase(request.getName())) {
            Optional<Product> existingProduct = Optional.ofNullable(productRepository.findByNameIgnoreCase(request.getName()));
            if (existingProduct.isPresent()) {
                throw new ApiException("A product with the same name already exists", ErrorCode.ITEM_ALREADY_EXISTS, HttpStatus.CONFLICT);
            }
        }

        // Update the product's fields with the new values
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());

        // Save the updated product to the database
        product = productRepository.save(product);

        // Return the updated product information
        return UpdateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(
                        product.getMultimedia() != null
                                ? multimediaStorageService.generatePublicUrl(product.getMultimedia().getUri())
                                : null
                )
                .build();
    }

    public void deleteProduct(UUID id) {
        // Attempt to find a product with the given ID
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)
        );

        // If the product is marked as deleted, it does not exist anymore
        if (product.isDeleted()) {
            throw new ApiException("A product with the given ID does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

        // Delete the product from the repository
        product.setDeleted(true);
        productRepository.save(product);
    }
}

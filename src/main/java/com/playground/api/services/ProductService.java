package com.playground.api.services;

import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.CreateProductBody;
import com.playground.api.dtos.product.CreateProductResponse;
import com.playground.api.dtos.product.ListProductsQuery;
import com.playground.api.dtos.product.ListProductsResponse;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import com.playground.api.models.Product;
import com.playground.api.repositories.ProductRepository;
import com.playground.api.repositories.specifications.ProductSpecifications;
import com.playground.api.utils.PaginationUtils;
import com.playground.api.utils.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CreateProductResponse createProduct(CreateProductBody request) {
        // Attempt to find a product with the same name
        Optional<Product> existingProduct = Optional.ofNullable(productRepository.findByNameIgnoreCase(request.getName()));

        // If a product with the same name exists, throw an exception
        if (existingProduct.isPresent()) {
            throw new Exception("A product with the same name already exists", ErrorCode.ITEM_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        // Create a new product entity
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        // Save the product to the database
        product = productRepository.save(product);

        // Return the created product
       return new CreateProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getImageUrl()
        );
    }

    public PaginationResponse<ListProductsResponse> listProducts(
            ListProductsQuery request
    ) {
        // Create a specification for filtering products
        Specification<Product> specification = Specification.where(null);

        // Add filters to the specification based on the request
        specification
                .and(SpecificationUtils.optional(request.getName(), ProductSpecifications::nameContains))
                .and(SpecificationUtils.optional(request.getCategory(), ProductSpecifications::inCategory))
                .and(SpecificationUtils.optional(request.getHasStock(), ProductSpecifications::hasStock))
                .and(SpecificationUtils.optional(request.getMinPrice(), ProductSpecifications::priceAtLeast))
                .and(SpecificationUtils.optional(request.getMaxPrice(), ProductSpecifications::priceAtMost));

        // Fetch all the products from the repository with given filters
        Page<Product> page = productRepository.findAll(
                specification,
                PaginationUtils.getPaginationFilters(request)
        );

        // Map the products to the response DTO
        List<ListProductsResponse> response = page.getContent().stream().map(
                product -> new ListProductsResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getStockQuantity(),
                        product.getImageUrl()
                )
        ).toList();

        // Create pagination response and return it
        return PaginationUtils.getPaginationResponse(page, response);
    }
}

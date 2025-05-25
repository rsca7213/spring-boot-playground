package com.playground.api.services;

import com.playground.api.dtos.common.PaginationQuery;
import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.CreateProductBody;
import com.playground.api.dtos.product.CreateProductResponse;
import com.playground.api.dtos.product.ListProductsResponse;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ConflictException;
import com.playground.api.mappers.CreateProductMapper;
import com.playground.api.mappers.ListProductsMapper;
import com.playground.api.models.Product;
import com.playground.api.repositories.ProductRepository;
import com.playground.api.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CreateProductMapper createProductMapper;
    private final ListProductsMapper listProductsMapper;

    @Autowired
    public ProductService(final ProductRepository productRepository, final CreateProductMapper createProductMapper, final ListProductsMapper listProductsMapper) {
        this.productRepository = productRepository;
        this.createProductMapper = createProductMapper;
        this.listProductsMapper = listProductsMapper;
    }

    public CreateProductResponse createProduct(CreateProductBody request) {
        // Attempt to find a product with the same name
        Optional<Product> existingProduct = Optional.ofNullable(productRepository.findByNameIgnoreCase(request.getName()));

        // If a product with the same name exists, throw an exception
        if (existingProduct.isPresent()) {
            throw new ConflictException("A product with the same name already exists", ErrorCode.ITEM_ALREADY_EXISTS);
        }

        // Create a new product entity
        Product product = createProductMapper.requestToModel(request);

        // Save the product to the database
        product = productRepository.save(product);

        // Create and return the response DTO
        return createProductMapper.modelToResponse(product);
    }

    public PaginationResponse<ListProductsResponse> listProducts(
            PaginationQuery pagination
    ) {
        // Fetch all the from the repository
        Page<Product> products = productRepository.findAll(
                PaginationUtils.getPaginationFilters(pagination)
        );

        // Map the products to the response DTO
        List<ListProductsResponse> response = listProductsMapper.modelToResponse(products.getContent());

        // Create pagination response and return it
        return PaginationUtils.getPaginationResponse(products, response);
    }
}

package com.playground.api.services;

import com.playground.api.dtos.product.CreateProductRequestBodyDto;
import com.playground.api.dtos.product.CreateProductResponseBodyDto;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ConflictException;
import com.playground.api.mappers.CreateProductMapper;
import com.playground.api.models.Product;
import com.playground.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CreateProductMapper createProductMapper;

    @Autowired
    public ProductService(final ProductRepository productRepository, final CreateProductMapper createProductMapper) {
        this.productRepository = productRepository;
        this.createProductMapper = createProductMapper;
    }

    public CreateProductResponseBodyDto createProduct(CreateProductRequestBodyDto request) {
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
}

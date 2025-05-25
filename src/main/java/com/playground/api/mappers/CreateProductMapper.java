package com.playground.api.mappers;

import com.playground.api.dtos.product.CreateProductBody;
import com.playground.api.dtos.product.CreateProductResponse;
import com.playground.api.models.Product;
import org.springframework.stereotype.Component;

@Component
public class CreateProductMapper implements RequestMapper<CreateProductBody, CreateProductResponse, Product> {
    @Override
    public Product requestToModel(CreateProductBody request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());

        return product;
    }

    @Override
    public CreateProductResponse modelToResponse(Product product) {
        return new CreateProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory(), product.getStockQuantity(), product.getImageUrl());
    }
}
package com.playground.api.mappers;

import com.playground.api.dtos.product.CreateProductRequestBodyDto;
import com.playground.api.dtos.product.CreateProductResponseBodyDto;
import com.playground.api.models.Product;
import org.springframework.stereotype.Component;

@Component
public class CreateProductMapper implements RequestMapper<CreateProductRequestBodyDto, CreateProductResponseBodyDto, Product> {
    @Override
    public Product requestToModel(CreateProductRequestBodyDto request) {
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
    public Class<CreateProductRequestBodyDto> getRequestType() {
        return CreateProductRequestBodyDto.class;
    }

    @Override
    public CreateProductResponseBodyDto modelToResponse(Product product) {
        return new CreateProductResponseBodyDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory(), product.getStockQuantity(), product.getImageUrl());
    }

    @Override
    public Class<CreateProductResponseBodyDto> getResponseType() {
        return CreateProductResponseBodyDto.class;
    }
}
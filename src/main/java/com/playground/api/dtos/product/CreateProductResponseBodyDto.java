package com.playground.api.dtos.product;

import com.playground.api.enums.ProductCategory;
import lombok.Value;

import java.util.UUID;

@Value
public class CreateProductResponseBodyDto {
    UUID id;
    String name;
    String description;
    Double price;
    ProductCategory category;
    Integer stockQuantity;
    String imageUrl;
}

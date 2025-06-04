package com.playground.api.dtos.product;

import com.playground.api.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@Schema(description = "Response object for creating a product")
public class CreateProductResponse {
    @Schema(
            description = "Unique identifier for the product",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID id;

    @Schema(
            description = "Name of the product",
            example = "Burger"
    )
    String name;

    @Schema(
            description = "Description of the product",
            example = "Delicious beef burger with cheese and lettuce"
    )
    String description;

    @Schema(
            description = "Price of the product in USD",
            example = "9.99"
    )
    Double price;

    @Schema(
            description = "Category of the product",
            example = "FOOD"
    )
    ProductCategory category;

    @Schema(
            description = "Quantity of the product available in stock",
            example = "50"
    )
    Integer stockQuantity;

    @Schema(
            description = "URL of the product image",
            example = "https://example.com/images/burger.jpg",
            nullable = true
    )
    String imageUrl;
}

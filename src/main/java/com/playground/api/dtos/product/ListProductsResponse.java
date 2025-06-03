package com.playground.api.dtos.product;

import com.playground.api.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
@Schema(description = "Response object for each item in the list of products")
public class ListProductsResponse {
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
}

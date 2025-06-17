package com.playground.api.dtos.product;

import com.playground.api.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Request body for updating an existing product")
public class UpdateProductBody {
    @Schema(
            description = "The name of the product",
            example = "Burger"
    )
    @NotBlank
    @Size(min = 3, max = 100)
    String name;

    @Schema(
            description = "A detailed description of the product",
            example = "A delicious burger with fresh ingredients"
    )
    @NotBlank
    @Size(min = 10, max = 500)
    String description;

    @Schema(
            description = "The amount available in stock",
            example = "5"
    )
    @NotNull
    @PositiveOrZero
    Integer stockQuantity;

    @Schema(
            description = "The category of the product",
            example = "FOOD"
    )
    @NotNull
    ProductCategory category;

    @Schema(
            description = "The price in USD of the product",
            example = "9.99"
    )
    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    Double price;
}

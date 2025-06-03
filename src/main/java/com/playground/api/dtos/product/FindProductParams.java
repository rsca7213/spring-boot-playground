package com.playground.api.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
@Schema(description = "Parameters for finding a product by ID")
public class FindProductParams {
    @Schema(
            description = "The unique identifier of the product to find",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID id;
}

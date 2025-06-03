package com.playground.api.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
@Schema(description = "Response object for uploading a product image")
public class UploadProductImageResponse {
    @Schema(
            description = "Unique identifier for the product",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID productId;

    @Schema(
            description = "URL of the uploaded product image",
            example = "https://example.com/images/product.jpg"
    )
    String url;
}

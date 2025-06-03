package com.playground.api.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
@Schema(description = "Parameters for uploading an image for a product")
public class UploadProductImageParams {
    @Schema(
            description = "The unique identifier of the product to upload the image for",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID productId;
}

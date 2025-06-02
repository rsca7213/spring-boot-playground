package com.playground.api.dtos.product;

import lombok.Value;

import java.util.UUID;

@Value
public class UploadProductImageResponse {
    UUID productId;
    String url;
}

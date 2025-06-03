package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
@Schema(description = "Response object for user registration")
public class RegisterUserResponse {
    @Schema(
            description = "The unique identifier of the registered user",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID id;
}

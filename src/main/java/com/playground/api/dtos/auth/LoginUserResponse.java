package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Response object for logging in a user")
public class LoginUserResponse {
    @Schema(
            description = "The JWT token for the authenticated user",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    String token;
}

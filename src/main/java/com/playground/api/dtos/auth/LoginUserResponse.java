package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Response object for logging in a user")
public class LoginUserResponse {
    @Schema(
            description = "The JWT token for the authenticated user (only returned if httpOnlyCookie is false)",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            nullable = true
    )
    String token;
}

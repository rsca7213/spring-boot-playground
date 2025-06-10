package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Response object for logging in a user")
public class LoginUserResponse {
    @Schema(
            description = "Indicates whether the login was successful",
            example = "true"
    )
    Boolean success;
}

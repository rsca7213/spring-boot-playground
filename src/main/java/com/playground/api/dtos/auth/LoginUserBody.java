package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
@Schema(description = "Request body for logging in a user")
public class LoginUserBody {
    @Schema(
            description = "The email address of the user",
            example = "johndoe@email.com"
    )
    @NotBlank
    @Email
    String email;

    @Schema(
            description = "The password for the user account",
            example = "Password123*"
    )
    @NotBlank
    @Size(min = 10)
    String password;
}

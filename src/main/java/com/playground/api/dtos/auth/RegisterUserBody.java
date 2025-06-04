package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@Schema(description = "Request body for registering a new user")
public class RegisterUserBody {
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

    @Schema(
            description = "The first name of the user",
            example = "John"
    )
    @NotBlank
    @Size(min = 3, max = 50)
    String firstName;

    @Schema(
            description = "The last name of the user",
            example = "Doe"
    )
    @NotBlank
    @Size(min = 3, max = 50)
    String lastName;

    @Schema(
            description = "The id of the role assigned to the user",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    @NotNull
    UUID roleId;
}

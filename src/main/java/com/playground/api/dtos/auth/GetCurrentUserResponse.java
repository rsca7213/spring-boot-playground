package com.playground.api.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@Schema(description = "Response object for getting the current user")
public class GetCurrentUserResponse {
    @Schema(
            description = "The unique identifier of the user",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID id;

    @Schema(
            description = "The email address of the user",
            example = "johndoe@email.com"
    )
    String email;

    @Schema(
            description = "The first name of the user",
            example = "John"
    )
    String firstName;

    @Schema(
            description = "The last name of the user",
            example = "Doe"
    )
    String lastName;

    @Schema(
            description = "The role of the user",
            example = "ADMIN"
    )
    String roleName;
}

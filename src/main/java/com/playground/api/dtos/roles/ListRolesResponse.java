package com.playground.api.dtos.roles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@Schema(description = "Response object for each item in the list of roles")
public class ListRolesResponse {
    @Schema(
            description = "Unique identifier for the role",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    UUID id;

    @Schema(
            description = "Name of the role",
            example = "ADMIN"
    )
    String name;
}

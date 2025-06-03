package com.playground.api.dtos.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationQuery {
    @Min(value = 1)
    @Schema(
            description = "The page number to retrieve, starting from 1",
            example = "1"
    )
    Integer page = 1;

    @Min(value = 1)
    @Schema(
            description = "The number of items to retrieve per page",
            example = "10"
    )
    Integer size = 10;
}

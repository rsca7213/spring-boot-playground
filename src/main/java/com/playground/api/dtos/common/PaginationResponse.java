package com.playground.api.dtos.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {
    @Schema(
            description = "The current page number",
            example = "1"
    )
    private final Integer page;

    @Schema(
            description = "The number of items per page",
            example = "10"
    )
    private final Integer perPage;

    @Schema(
            description = "The total number of items across all pages",
            example = "100"
    )
    private final Long totalItems;

    @Schema(
            description = "The total number of pages available",
            example = "10"
    )
    private final Integer totalPages;

    @Schema(
            description = "Indicates if this is the first page",
            example = "true"
    )
    private final Boolean isFirstPage;

    @Schema(
            description = "Indicates if this is the last page",
            example = "false"
    )
    private final Boolean isLastPage;

    @Schema(
            description = "Indicates if there is a next page",
            example = "true"
    )
    private final Boolean hasNextPage;

    @Schema(
            description = "Indicates if there is a previous page",
            example = "false"
    )
    private final Boolean hasPreviousPage;

    @Schema(
            description = "The list of items on the current page"
    )
    private List<T> items;
}

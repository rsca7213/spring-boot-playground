package com.playground.api.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {
    private final Integer page;
    private final Integer perPage;
    private final Long totalItems;
    private final Integer totalPages;
    private final Boolean isFirstPage;
    private final Boolean isLastPage;
    private final Boolean hasNextPage;
    private final Boolean hasPreviousPage;

    private List<T> items;
}

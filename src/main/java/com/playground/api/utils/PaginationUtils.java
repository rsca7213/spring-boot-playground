package com.playground.api.utils;

import com.playground.api.dtos.common.PaginationQuery;
import com.playground.api.dtos.common.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationUtils {
    public static Pageable getPaginationFilters(PaginationQuery paginationQuery) {
        return org.springframework.data.domain.PageRequest.of(
                paginationQuery.getPage() - 1,
                paginationQuery.getSize()
        );
    }

    public static <P, T> PaginationResponse<T> getPaginationResponse(Page<P> page, List<T> items) {
        PaginationResponse<T> pagination = new PaginationResponse<>(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );

        pagination.setItems(items);

        return pagination;
    }
}

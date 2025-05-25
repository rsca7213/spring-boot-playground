package com.playground.api.dtos.common;

import jakarta.validation.constraints.Min;
import lombok.Value;

@Value
public class PaginationQuery {
    @Min(value = 1)
    Integer page = 1;

    @Min(value = 1)
    Integer size = 10;
}

package com.playground.api.dtos.common;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationQuery {
    @Min(value = 1)
    Integer page = 1;

    @Min(value = 1)
    Integer size = 10;
}

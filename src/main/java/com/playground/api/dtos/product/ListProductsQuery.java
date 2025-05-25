package com.playground.api.dtos.product;

import com.playground.api.dtos.common.PaginationQuery;
import com.playground.api.enums.ProductCategory;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ListProductsQuery extends PaginationQuery {
    @Min(value = 1)
    String name;

    Boolean hasStock;

    @Enumerated
    ProductCategory category;

    @DecimalMin(value = "0.01")
    Double minPrice;

    @DecimalMin(value = "0.01")
    Double maxPrice;

    @AssertTrue(message = "The minimum price cannot be higher than the maximum price")
    public boolean isPriceRangeValid() {
        if (minPrice == null || maxPrice == null) return true;
        return minPrice <= maxPrice;
    }
}

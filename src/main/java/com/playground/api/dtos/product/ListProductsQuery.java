package com.playground.api.dtos.product;

import com.playground.api.dtos.common.PaginationQuery;
import com.playground.api.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@Builder
@Schema(description = "Query parameters for listing and paginating products")
public class ListProductsQuery extends PaginationQuery {
    @Min(value = 1)
    @Schema(
            description = "The name of the product to filter by",
            example = "Burger"
    )
    String name;

    @Schema(
            description = "If the product has stock available",
            example = "true"
    )
    Boolean hasStock;

    @Enumerated
    @Schema(
            description = "The category of the product to filter by",
            example = "FOOD"
    )
    ProductCategory category;

    @DecimalMin(value = "0.01")
    @Schema(
            description = "The minimum price of the product to filter by",
            example = "5.00"
    )
    Double minPrice;

    @DecimalMin(value = "0.01")
    @Schema(
            description = "The maximum price of the product to filter by",
            example = "100.00"
    )
    Double maxPrice;

    @AssertTrue(message = "The minimum price cannot be higher than the maximum price")
    public boolean isPriceRangeValid() {
        if (minPrice == null || maxPrice == null) return true;
        return minPrice <= maxPrice;
    }
}

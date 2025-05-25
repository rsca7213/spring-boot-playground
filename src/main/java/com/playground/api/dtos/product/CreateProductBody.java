package com.playground.api.dtos.product;

import com.playground.api.enums.ProductCategory;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Value;
import org.hibernate.validator.constraints.URL;

@Value
public class CreateProductBody {
    @NotBlank
    @Size(min = 3, max = 100)
    String name;

    @NotBlank
    @Size(min = 10, max = 500)
    String description;

    @NotNull
    @PositiveOrZero
    Integer stockQuantity;

    @NotNull
    @Enumerated
    ProductCategory category;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    Double price;

    @URL
    String imageUrl;
}

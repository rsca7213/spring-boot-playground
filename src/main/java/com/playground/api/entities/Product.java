package com.playground.api.entities;

import com.playground.api.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    @Size(min = 10, max = 500)
    private String description;

    @NotNull
    @PositiveOrZero
    private Integer stockQuantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "multimedia_id")
    private Multimedia multimedia;
}

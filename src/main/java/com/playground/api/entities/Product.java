package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import com.playground.api.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product extends DataEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @NotBlank
    @Size(min = 10, max = 500)
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @PositiveOrZero
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @NotNull
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "multimedia_id")
    private Multimedia multimedia;
}

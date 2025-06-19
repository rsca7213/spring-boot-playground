package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "policies")
public class Policy extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Positive
    @DecimalMin(value = "0.00")
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @NotNull
    @Column(name = "expedition_date", nullable = false)
    private LocalDate expeditionDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @OneToMany(mappedBy = "policy")
    private Set<Claim> claims;

    @OneToMany(mappedBy = "policy")
    private Set<PolicyCoverage> coverages;
}

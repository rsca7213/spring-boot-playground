package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import com.playground.api.entities.identifiers.ClaimCoverageId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "claims_coverages")
@IdClass(ClaimCoverageId.class)
public class ClaimCoverage extends DataEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Id
    @ManyToOne
    @JoinColumn(name = "coverage_id", nullable = false)
    private Coverage coverage;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
}

package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import com.playground.api.entities.identifiers.ClaimCoverageId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "claims_coverages")
@IdClass(ClaimCoverageId.class)
public class ClaimCoverage extends DataEntity {
    @Id
    @Column(name = "claim_id", nullable = false)
    private Integer claimId;

    @Id
    @Column(name = "coverage_id", nullable = false)
    private Integer coverageId;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    @Column(name = "amount", nullable = false)
    private Double amount;
}

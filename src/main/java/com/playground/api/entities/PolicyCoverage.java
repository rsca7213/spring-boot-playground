package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import com.playground.api.entities.identifiers.PolicyCoverageId;
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
@Table(name = "policies_coverages")
@IdClass(PolicyCoverageId.class)
public class PolicyCoverage extends DataEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Id
    @ManyToOne
    @JoinColumn(name = "coverage_id", nullable = false)
    private Coverage coverage;

    @NotNull
    @Positive
    @DecimalMin(value = "0.00")
    @Column(name = "limit", nullable = false)
    private BigDecimal limit;
}

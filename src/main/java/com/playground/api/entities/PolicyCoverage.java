package com.playground.api.entities;

import com.playground.api.entities.identifiers.PolicyCoverageId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "policies_coverages")
@IdClass(PolicyCoverageId.class)
public class PolicyCoverage {
    @Id
    @Column(name = "policy_id", nullable = false)
    private Integer policyId;

    @Id
    @Column(name = "coverage_id", nullable = false)
    private Integer coverageId;

    @NotNull
    @Positive
    @DecimalMin(value = "0.00")
    @Column(name = "limit", nullable = false)
    private Double limit;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @PrePersist
    private void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

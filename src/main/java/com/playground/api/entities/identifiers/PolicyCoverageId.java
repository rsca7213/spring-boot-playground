package com.playground.api.entities.identifiers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCoverageId implements Serializable {
    private Integer policyId;
    private Integer coverageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PolicyCoverageId that)) return false;

        return policyId.equals(that.policyId) && coverageId.equals(that.coverageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyId, coverageId);
    }
}

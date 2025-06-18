package com.playground.api.entities.identifiers;

import com.playground.api.entities.Coverage;
import com.playground.api.entities.Policy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyCoverageId implements Serializable {
    private Policy policy;
    private Coverage coverage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PolicyCoverageId that)) return false;

        return policy.getId().equals(that.policy.getId()) && coverage.getId().equals(that.coverage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(policy.getId(), coverage.getId());
    }
}

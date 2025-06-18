package com.playground.api.entities.identifiers;

import com.playground.api.entities.Claim;
import com.playground.api.entities.Coverage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimCoverageId implements Serializable {
    private Claim claim;
    private Coverage coverage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClaimCoverageId that)) return false;

        return claim.getId().equals(that.claim.getId()) && coverage.getId().equals(that.coverage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(claim.getId(), coverage.getId());
    }
}

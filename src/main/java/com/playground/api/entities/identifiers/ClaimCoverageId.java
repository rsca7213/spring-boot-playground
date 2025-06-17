package com.playground.api.entities.identifiers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimCoverageId implements Serializable {
    private Integer claimId;
    private Integer coverageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClaimCoverageId that)) return false;

        return claimId.equals(that.claimId) && coverageId.equals(that.coverageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimId, coverageId);
    }
}

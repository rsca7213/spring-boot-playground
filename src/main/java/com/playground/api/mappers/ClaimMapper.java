package com.playground.api.mappers;

import com.playground.api.dtos.claims.CreateClaimResponse;
import com.playground.api.entities.Claim;
import com.playground.api.entities.ClaimCoverage;
import com.playground.api.entities.Policy;
import com.playground.api.utils.ClaimUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
public class ClaimMapper {
    private final ClaimUtils claimUtils;

    @Autowired
    public ClaimMapper(ClaimUtils claimUtils) {
        this.claimUtils = claimUtils;
    }


    public CreateClaimResponse claimToCreateClaimResponse(Claim claim, BigDecimal totalClaimedAmount) {
        Policy policy = claim.getPolicy();
        Stream<ClaimCoverage> coverages = claim.getCoverages().stream();

        return CreateClaimResponse.builder()
                .claimDetails(
                        CreateClaimResponse.ClaimDetails.builder()
                                .id(claim.getId())
                                .number(claim.getNumber())
                                .date(claim.getClaimDate())
                                .totalAmount(
                                        CreateClaimResponse.TotalAmount.builder()
                                                .uf(totalClaimedAmount)
                                                .clp(claimUtils.currencyUfToClp(totalClaimedAmount))
                                                .build()
                                )
                                .build()
                )
                .policyDetails(
                        CreateClaimResponse.PolicyDetails.builder()
                                .id(claim.getPolicy().getId())
                                .remainingBalance(
                                        CreateClaimResponse.TotalAmount.builder()
                                                .uf(policy.getBalance())
                                                .clp(claimUtils.currencyUfToClp(policy.getBalance()))
                                                .build()
                                )
                                .build()
                )
                .damages(coverages.map(
                        coverage -> CreateClaimResponse.ClaimDamageItem.builder()
                                .coverageName(coverage.getCoverage().getName())
                                .amount(
                                        CreateClaimResponse.TotalAmount.builder()
                                                .uf(coverage.getAmount())
                                                .clp(claimUtils.currencyUfToClp(coverage.getAmount()))
                                                .build()
                                )
                                .build()
                ).toList())
                .build();
    }
}

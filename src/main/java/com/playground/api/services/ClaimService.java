package com.playground.api.services;

import com.playground.api.dtos.claims.CreateClaimBody;
import com.playground.api.dtos.claims.CreateClaimResponse;
import com.playground.api.entities.*;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import com.playground.api.repositories.ClaimCoverageRepository;
import com.playground.api.repositories.ClaimRepository;
import com.playground.api.repositories.PolicyRepository;
import com.playground.api.utils.ClaimUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimCoverageRepository claimCoverageRepository;
    private final PolicyRepository policyRepository;
    private final ClaimUtils claimUtils;

    @Autowired
    public ClaimService(
            ClaimRepository claimRepository,
            ClaimCoverageRepository claimCoverageRepository,
            PolicyRepository policyRepository,
            ClaimUtils claimUtils
    ) {
        this.claimRepository = claimRepository;
        this.claimCoverageRepository = claimCoverageRepository;
        this.policyRepository = policyRepository;
        this.claimUtils = claimUtils;
    }

    @Transactional
    public CreateClaimResponse createClaim(CreateClaimBody request) {
        BigDecimal totalClaimedAmount = BigDecimal.ZERO;

        // Verify that the policy exists
        Policy policy = policyRepository.findById(request.getPolicyId()).orElseThrow(
                () -> new Exception(
                        "The policy with the given ID does not exist",
                        ErrorCode.ITEM_DOES_NOT_EXIST,
                        HttpStatus.NOT_FOUND
                )
        );

        // Count the number of claims for the policy
        Integer claimCount = claimRepository.countByPolicyId(policy.getId());

        // Generate a Map for each PolicyCoverage to quickly access it by coverage ID
        Map<Integer, PolicyCoverage> policyCoverages = new HashMap<>();

        // Verify each coverage reported damage in the claim
        for (CreateClaimBody.ClaimDamageItem damage : request.getDamages()) {
            // Update the total claimed amount
            totalClaimedAmount = totalClaimedAmount.add(damage.getAmount());

            // Get the policy coverage associated with the policy
            PolicyCoverage policyCoverage = policy.getCoverages().stream()
                    .filter(pc -> pc.getCoverage().getId().equals(damage.getCoverageId()))
                    .findFirst()
                    .orElse(null);

            // If the coverage does not exist, throw an exception
            if (policyCoverage == null) {
                throw new Exception(
                        "The coverage is not available in the policy",
                        ErrorCode.ITEM_DOES_NOT_EXIST,
                        HttpStatus.NOT_FOUND
                );
            }

            // Store the policy coverage in the map for quick access
            policyCoverages.put(damage.getCoverageId(), policyCoverage);

            // Verify that the limit of the coverage is not exceeded
            if (damage.getAmount().compareTo(policyCoverage.getLimit()) > 0) {
                throw new Exception(
                        "The amount exceeds the limit of the coverage",
                        ErrorCode.LIMIT_EXCEEDED,
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        // Verify that the total claimed amount does not exceed the available policy balance
        if (totalClaimedAmount.compareTo(policy.getBalance()) > 0) {
            throw new Exception(
                    "The total claimed amount exceeds the available policy balance",
                    ErrorCode.LIMIT_EXCEEDED,
                    HttpStatus.BAD_REQUEST
            );
        }

        // Create a new claim entity from the request body
        Claim claim = new Claim();

        claim.setPolicy(policy);
        claim.setClaimDate(request.getClaimDate());
        claim.setNumber(claimUtils.generateClaimNumber(policy.getId(), request.getClaimDate(), claimCount));

        // Create ClaimCoverage entities for each damage in the claim
        List<ClaimCoverage> claimCoverages = request.getDamages().stream()
                .map(damage -> {
                    ClaimCoverage claimCoverage = new ClaimCoverage();
                    claimCoverage.setClaim(claim);
                    claimCoverage.setAmount(damage.getAmount());
                    claimCoverage.setCoverage(policyCoverages.get(damage.getCoverageId()).getCoverage());

                    return claimCoverage;
                }).toList();

        // Save the claim entity to the repository
        final Claim generatedClaim = claimRepository.save(claim);

        // Assign the coverages to the claim
        claimCoverages.forEach(claimCoverage -> claimCoverage.setClaim(generatedClaim));
        generatedClaim.setCoverages(new HashSet<>(claimCoverages));

        // Save the claim coverages to the repository
        claimCoverageRepository.saveAll(claimCoverages);

        // Reduce the policy balance by the total claimed amount
        policy.setBalance(policy.getBalance().add(totalClaimedAmount.negate()));
        policyRepository.save(policy);

        // Return the claim response object
        return CreateClaimResponse.builder()
                .claimDetails(
                        CreateClaimResponse.ClaimDetails.builder()
                                .id(generatedClaim.getId())
                                .number(generatedClaim.getNumber())
                                .date(generatedClaim.getClaimDate())
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
                                .id(policy.getId())
                                .remainingBalance(
                                        CreateClaimResponse.TotalAmount.builder()
                                                .uf(policy.getBalance())
                                                .clp(claimUtils.currencyUfToClp(policy.getBalance()))
                                                .build()
                                )
                                .build()
                )
                .damages(generatedClaim.getCoverages().stream().map(
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

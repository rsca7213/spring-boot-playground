package com.playground.api.services;

import com.playground.api.dtos.claims.CreateClaimBody;
import com.playground.api.dtos.claims.CreateClaimResponse;
import com.playground.api.entities.*;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import com.playground.api.mappers.ClaimMapper;
import com.playground.api.repositories.*;
import com.playground.api.utils.ClaimUtils;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;
import jakarta.transaction.Transactional;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.ConsequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimCoverageRepository claimCoverageRepository;
    private final PolicyCoverageRepository policyCoverageRepository;
    private final PolicyRepository policyRepository;
    private final ClaimUtils claimUtils;
    private final ClaimMapper claimMapper;
    private final KieContainer kieContainer;
    private final ZeebeClient zeebeClient;
    private final CoverageRepository coverageRepository;

    @Autowired
    public ClaimService(
            ClaimRepository claimRepository,
            ClaimCoverageRepository claimCoverageRepository,
            PolicyRepository policyRepository,
            ClaimUtils claimUtils,
            ClaimMapper claimMapper,
            KieContainer kieContainer,
            ZeebeClient zeebeClient,
            PolicyCoverageRepository policyCoverageRepository,
            CoverageRepository coverageRepository) {
        this.claimRepository = claimRepository;
        this.claimCoverageRepository = claimCoverageRepository;
        this.policyRepository = policyRepository;
        this.claimUtils = claimUtils;
        this.claimMapper = claimMapper;
        this.kieContainer = kieContainer;
        this.zeebeClient = zeebeClient;
        this.policyCoverageRepository = policyCoverageRepository;
        this.coverageRepository = coverageRepository;
    }

    @Transactional
    public CreateClaimResponse createClaim(CreateClaimBody request) {
        BigDecimal totalClaimedAmount = BigDecimal.ZERO;

        // Verify that the policy exists
        Policy policy = policyRepository.findById(request.getPolicyId()).orElseThrow(
                () -> new ApiException(
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
                throw new ApiException(
                        "The coverage is not available in the policy",
                        ErrorCode.ITEM_DOES_NOT_EXIST,
                        HttpStatus.NOT_FOUND
                );
            }

            // Store the policy coverage in the map for quick access
            policyCoverages.put(damage.getCoverageId(), policyCoverage);

            // Verify that the limit of the coverage is not exceeded
            if (damage.getAmount().compareTo(policyCoverage.getLimit()) > 0) {
                throw new ApiException(
                        "The amount exceeds the limit of the coverage",
                        ErrorCode.LIMIT_EXCEEDED,
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        // Verify that the total claimed amount does not exceed the available policy balance
        if (totalClaimedAmount.compareTo(policy.getBalance()) > 0) {
            throw new ApiException(
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
        return claimMapper.claimToCreateClaimResponse(generatedClaim, totalClaimedAmount);
    }

    @Transactional
    public CreateClaimResponse createClaimDrools(CreateClaimBody request) {
        BigDecimal totalClaimedAmount = BigDecimal.ZERO;

        // Verify that the policy exists
        Policy policy = policyRepository.findById(request.getPolicyId()).orElseThrow(
                () -> new ApiException(
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

            // Store the policy coverage in the map for quick access
            policyCoverages.put(damage.getCoverageId(), policyCoverage);
        }

        // Apply Drools business rules to evaluate the claim creation
        try (KieSession kieSession = kieContainer.newKieSession()) {
            kieSession.insert(policy);
            request.getDamages().forEach(kieSession::insert);
            policyCoverages.forEach((id, policyCoverage) -> kieSession.insert(policyCoverage));
            kieSession.insert(totalClaimedAmount);
            kieSession.fireAllRules();
            kieSession.dispose();
        } catch (ConsequenceException ce) {
            Throwable cause = ce.getCause();

            if (cause instanceof ApiException apiException) {
                throw apiException;
            } else {
                throw new ApiException(
                        "An unexpected error occurred while processing the information",
                        ErrorCode.RUNTIME_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
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
        return claimMapper.claimToCreateClaimResponse(generatedClaim, totalClaimedAmount);
    }

    public Object createClaimCamunda(CreateClaimBody request) {
        ProcessInstanceResult eventResult = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("create-claim-camunda")
                .latestVersion()
                .variables(Map.of(
                                "request", request
                        )
                )
                .withResult()
                .send()
                .join();

        Map<String, Object> variables = eventResult.getVariablesAsMap();

        if (variables.containsKey("error")) {
            throw new ApiException(
                    (String) variables.get("error"),
                    ErrorCode.valueOf((String) variables.get("errorCode")),
                    HttpStatus.valueOf((String) variables.get("httpCode"))
            );
        }

        return variables.get("response");
    }

    @Transactional
    public CreateClaimResponse createClaimDroolsAndCamunda(CreateClaimBody request) {
        return CreateClaimResponse.builder().build();
    }

    @JobWorker(type = "claim-find-policy-by-id")
    @Transactional
    public Map<String, Object> camundaFindClaimPolicyId(@Variable("policyId") Integer policyId) {
        Policy policy = policyRepository.findById(policyId).orElseThrow(
                () -> new ZeebeBpmnError(
                        "API_EXCEPTION",
                        "API_EXCEPTION",
                        Map.of(
                                "error", String.format("The policy by the ID %s does not exist", policyId),
                                "errorCode", ErrorCode.ITEM_DOES_NOT_EXIST.name(),
                                "httpCode", HttpStatus.NOT_FOUND.name()
                        )
                )
        );


        List<PolicyCoverage> policyCoverages = policyCoverageRepository.findByPolicyId(policy.getId());
        Integer claimCount = claimRepository.countByPolicyId(policyId);


        return Map.of(
                "policy", Map.of(
                        "id", policy.getId(),
                        "balance", policy.getBalance(),
                        "expeditionDate", policy.getExpeditionDate(),
                        "expirationDate", policy.getExpirationDate()
                ),
                "policyCoverages", policyCoverages.stream().map(pc -> Map.of(
                        "name", pc.getCoverage().getName(),
                        "coverageId", pc.getCoverage().getId(),
                        "limit", pc.getLimit()
                )).toList(),
                "policyClaimCount", claimCount
        );
    }

    @JobWorker(type = "claim-save-claim-creation-data")
    @Transactional
    public Map<String, Object> camundaSaveCreateClaimData(
            @Variable("request") CreateClaimBody request,
            @Variable("policy") Policy policy,
            @Variable("totalClaimedAmount") Double totalClaimedAmount,
            @Variable("generatedClaimNumber") String generatedClaimNumber
    ) {
        // Reduce the policy balance by the total claimed amount
        policy.setBalance(policy.getBalance().add(BigDecimal.valueOf(totalClaimedAmount).negate()));

        // Save the policy with the updated balance
        policyRepository.save(policy);

        // Create a new claim entity from the request body
        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setClaimDate(request.getClaimDate());
        claim.setNumber(generatedClaimNumber);
        final Claim generatedClaim = claimRepository.save(claim);

        // Find all relevant policy coverages
        List<Coverage> coverages = coverageRepository.findAllById(
                request.getDamages().stream()
                        .map(CreateClaimBody.ClaimDamageItem::getCoverageId)
                        .toList()
        );

        // Create ClaimCoverage entities for each damage in the claim
        List<ClaimCoverage> claimCoverages = request.getDamages().stream()
                .map(damage -> {
                    ClaimCoverage claimCoverage = new ClaimCoverage();
                    claimCoverage.setClaim(generatedClaim);
                    claimCoverage.setAmount(damage.getAmount());
                    claimCoverage.setCoverage(coverages.stream()
                            .filter(c -> c.getId().equals(damage.getCoverageId()))
                            .findFirst()
                            .orElseThrow(() -> new ZeebeBpmnError(
                                    "API_EXCEPTION",
                                    "API_EXCEPTION",
                                    Map.of(
                                            "error", "Coverage not found for ID: " + damage.getCoverageId(),
                                            "errorCode", ErrorCode.ITEM_DOES_NOT_EXIST.name(),
                                            "httpCode", HttpStatus.NOT_FOUND.name()
                                    )
                            )));

                    return claimCoverage;
                }).toList();

        claimCoverageRepository.saveAll(claimCoverages);

        return Map.of(
                "generatedClaimId", claim.getId()
        );
    }
}

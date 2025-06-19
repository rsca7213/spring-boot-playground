package com.playground.api.controllers;

import com.playground.api.entities.Policy;
import com.playground.api.services.PolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/policies")
@Tag(name = "Policies", description = "Endpoints for managing insurance policies")
public class PolicyController {
    private final PolicyService policyService;

    @Autowired
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<Boolean> evaluatePolicy(@RequestBody Policy policy) {
        Boolean evaluatedPolicy = policyService.evaluatePolicy(policy);
        return ResponseEntity.ok(evaluatedPolicy);
    }

}

package com.playground.api.controllers;

import com.playground.api.dtos.claims.CreateClaimBody;
import com.playground.api.dtos.claims.CreateClaimResponse;
import com.playground.api.services.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/claims")
@Tag(name = "Claims", description = "Endpoints for managing insurance claims")
public class ClaimController {
    private final ClaimService claimService;

    @Autowired
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Operation(
            summary = "Create a new claim (ADMIN, CLIENT)",
            description = "Creates a new claim with the provided details. Returns the created claim. (Roles: ADMIN, CLIENT)",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<CreateClaimResponse> createClaim(
            @Valid @RequestBody CreateClaimBody createClaimBody
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                claimService.createClaim(createClaimBody)
        );
    }
}

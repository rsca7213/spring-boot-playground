package com.playground.api.dtos.claims;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@Schema(description = "Response object when a claim is successfully created")
public class CreateClaimResponse {
    @Schema(description = "The details of the claim that was created")
    ClaimDetails claimDetails;

    @Schema(description = "The policy details associated with the claim")
    PolicyDetails policyDetails;

    @Schema(description = "A list of damages associated with the claim")
    List<ClaimDamageItem> damages;

    @Value
    @Builder
    @Schema(description = "Details of the claim that was created")
    public static class ClaimDetails {
        @Schema(description = "The unique identifier of the claim", example = "12345")
        Integer id;

        @Schema(description = "The unique number associated with the claim", example = "01102023-1-1")
        String number;

        @Schema(description = "The date when the claim was created", example = "2023-10-01")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDate date;

        @Schema(description = "The total amount of the claim in different currencies")
        TotalAmount totalAmount;


    }

    @Value
    @Builder
    @Schema(description = "Details of the damages associated with the claim")
    public static class ClaimDamageItem {
        @Schema(description = "Name of the coverage associated with the damage", example = "Damage to Third Party")
        String coverageName;

        @Schema(description = "Amount of the damage")
        TotalAmount amount;
    }

    @Value
    @Builder
    @Schema(description = "Details of the policy associated with the claim")
    public static class PolicyDetails {
        @Schema(description = "The unique identifier of the policy", example = "54321")
        Integer id;

        @Schema(description = "The remaining balance of the policy in different currencies")
        TotalAmount remainingBalance;
    }

    @Value
    @Builder
    @Schema(description = "Total amount in different currencies")
    public static class TotalAmount {
        @Schema(description = "The amount in UF", example = "250.00")
        BigDecimal uf;

        @Schema(description = "The amount in Chilean Pesos (CLP)", example = "300000.00")
        BigDecimal clp;
    }
}

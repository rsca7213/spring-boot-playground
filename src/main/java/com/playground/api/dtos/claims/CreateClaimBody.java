package com.playground.api.dtos.claims;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@Schema(description = "Request body required to create a new claim")
public class CreateClaimBody {
    @Schema(
            description = "The unique identifier of the policy associated with the claim",
            example = "12345",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Positive
    Integer policyId;

    @Schema(
            description = "The date when the claim happened",
            example = "2023-10-01",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @PastOrPresent
    LocalDate claimDate;

    @Schema(
            description = "Damages associated with the claim",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty
    @Valid
    List<ClaimDamageItem> damages;

    @Value
    @Builder
    @Schema(description = "Details of a specific damage within a claim")
    public static class ClaimDamageItem {
        @Schema(
                description = "Unique identifier for the coverage associated with the damage",
                example = "54321",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Positive
        Integer coverageId;

        @Schema(
                description = "Amount of the damage in UF",
                example = "250.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Positive
        @DecimalMin("0.01")
        Double amount;
    }
}

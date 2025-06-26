package com.playground.api.dtos.claims;

import com.playground.api.enums.CreateClaimProcess;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Query parameters to determine which claim creation process to follow")
public class CreateClaimQuery {
    @Schema(
            description = "The process to follow for creating a claim",
            example = "DROOLS_CAMUNDA"
    )
    CreateClaimProcess process;
}

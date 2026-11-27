package tn.esprit.ecocityservice.ecocity.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Data sent to raise an alert manually.
 * indicator, value and severity are copied from the reading and the threshold.
 */
public record AlertRequest(

        @NotNull(message = "measurementId is required")
        Long measurementId,

        @NotNull(message = "thresholdId is required")
        Long thresholdId
) {
}

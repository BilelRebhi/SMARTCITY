package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.AlertSeverity;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Data sent to create or update a threshold. unit defaults to the indicator's unit, active defaults to true. */
public record ThresholdRequest(

        @NotNull(message = "indicator is required")
        Indicator indicator,

        @NotNull(message = "maxValue is required")
        @Positive(message = "maxValue must be greater than 0")
        Double maxValue,

        String unit,

        @NotNull(message = "severity is required")
        AlertSeverity severity,

        Boolean active
) {
}

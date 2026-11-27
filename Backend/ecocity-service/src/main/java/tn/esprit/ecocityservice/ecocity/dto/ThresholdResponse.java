package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.AlertSeverity;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;

/** Threshold returned by the API. */
public record ThresholdResponse(
        Long id,
        Indicator indicator,
        Double maxValue,
        String unit,
        AlertSeverity severity,
        Boolean active
) {
}

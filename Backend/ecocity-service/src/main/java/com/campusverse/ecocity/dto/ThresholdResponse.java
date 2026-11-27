package com.campusverse.ecocity.dto;

import com.campusverse.ecocity.enums.AlertSeverity;
import com.campusverse.ecocity.enums.Indicator;

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

package com.campusverse.ecocity.dto;

import com.campusverse.ecocity.enums.AlertSeverity;
import com.campusverse.ecocity.enums.AlertStatus;
import com.campusverse.ecocity.enums.Indicator;

import java.time.LocalDateTime;

/** Pollution alert returned by the API. */
public record AlertResponse(
        Long id,
        Indicator indicator,
        Double value,
        Double thresholdValue,
        AlertSeverity severity,
        AlertStatus status,
        LocalDateTime triggeredAt,
        Long measurementId,
        Long thresholdId,
        Long stationId,
        Long zoneId
) {
}

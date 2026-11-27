package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.AlertSeverity;
import tn.esprit.ecocityservice.ecocity.enums.AlertStatus;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;

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

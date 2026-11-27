package tn.esprit.ecocityservice.ecocity.event;

import tn.esprit.ecocityservice.ecocity.enums.AlertSeverity;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;

import java.time.LocalDateTime;

/** Sent to MS2 (HIGH alerts open an incident) and MS3 (traffic-related peaks). Routing key ecocity.alert.raised. */
public record PollutionAlertRaised(
        String eventId,
        Long alertId,
        Long stationId,
        Long zoneId,
        Indicator indicator,
        Double value,
        Double thresholdValue,
        String unit,
        AlertSeverity severity,
        LocalDateTime triggeredAt
) {
}

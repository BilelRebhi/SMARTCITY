package tn.esprit.ecocityservice.ecocity.event;

import java.time.LocalDateTime;

/** Sent to MS2 and MS3 so they close the related follow-up. Routing key ecocity.alert.resolved. */
public record PollutionAlertResolved(
        String eventId,
        Long alertId,
        Long zoneId,
        LocalDateTime resolvedAt
) {
}

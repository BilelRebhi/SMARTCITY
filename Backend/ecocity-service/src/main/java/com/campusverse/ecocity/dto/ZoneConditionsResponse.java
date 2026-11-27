package com.campusverse.ecocity.dto;

import java.util.List;

/** Current conditions in a zone: the latest reading of each indicator and the number of open alerts. */
public record ZoneConditionsResponse(
        Long zoneId,
        String zoneName,
        List<ReadingResponse> latestReadings,
        long openAlerts
) {
}

package com.campusverse.ecocity.dto;

import java.time.LocalDate;

/** Environmental report returned by the API. */
public record EnvReportResponse(
        Long id,
        Long zoneId,
        String zoneName,
        LocalDate periodStart,
        LocalDate periodEnd,
        Double avgValue,
        Double maxValue
) {
}

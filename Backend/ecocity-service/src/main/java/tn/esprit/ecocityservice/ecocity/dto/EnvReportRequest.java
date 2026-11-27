package tn.esprit.ecocityservice.ecocity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

/** Data sent to create or update an environmental report. */
public record EnvReportRequest(

        @NotNull(message = "zoneId is required")
        Long zoneId,

        @NotNull(message = "periodStart is required")
        LocalDate periodStart,

        @NotNull(message = "periodEnd is required")
        LocalDate periodEnd,

        @PositiveOrZero(message = "avgValue cannot be negative")
        Double avgValue,

        @PositiveOrZero(message = "maxValue cannot be negative")
        Double maxValue
) {
}

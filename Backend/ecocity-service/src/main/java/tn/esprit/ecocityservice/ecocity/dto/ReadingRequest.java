package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import tn.esprit.ecocityservice.ecocity.enums.MeasurementQuality;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

/**
 * Data sent to record or update a reading (Measurement entity).
 * unit, recordedAt and quality are optional: they default to the indicator's unit, now, and VALID.
 */
public record ReadingRequest(

        @NotNull(message = "stationId is required")
        Long stationId,

        @NotNull(message = "indicator is required")
        Indicator indicator,

        @NotNull(message = "value is required")
        @PositiveOrZero(message = "value cannot be negative")
        Double value,

        String unit,

        @PastOrPresent(message = "recordedAt cannot be in the future")
        LocalDateTime recordedAt,

        MeasurementQuality quality
) {
}

package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import tn.esprit.ecocityservice.ecocity.enums.MeasurementQuality;

import java.time.LocalDateTime;

/** Reading (Measurement entity) returned by the API. */
public record ReadingResponse(
        Long id,
        Long stationId,
        String stationCode,
        Indicator indicator,
        Double value,
        String unit,
        LocalDateTime recordedAt,
        MeasurementQuality quality
) {
}

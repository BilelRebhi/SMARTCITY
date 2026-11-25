package com.campusverse.ecocity.dto;

import com.campusverse.ecocity.enums.Indicator;
import com.campusverse.ecocity.enums.MeasurementQuality;

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

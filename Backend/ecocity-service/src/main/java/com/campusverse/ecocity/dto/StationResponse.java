package com.campusverse.ecocity.dto;

import com.campusverse.ecocity.enums.StationStatus;

/** Monitoring station returned by the API. */
public record StationResponse(
        Long id,
        String code,
        String name,
        Double latitude,
        Double longitude,
        StationStatus status,
        Integer samplingIntervalSec,
        Long zoneId,
        String zoneName
) {
}

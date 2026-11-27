package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.StationStatus;

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

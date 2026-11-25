package com.campusverse.ecocity.dto;

import com.campusverse.ecocity.enums.StationStatus;
import jakarta.validation.constraints.*;

/** Data sent to create or update a monitoring station. status and samplingIntervalSec are optional. */
public record StationRequest(

        @NotBlank(message = "code is required")
        @Size(max = 30, message = "code must be at most 30 characters")
        String code,

        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @NotNull(message = "latitude is required")
        @DecimalMin(value = "-90.0", message = "latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "latitude must be between -90 and 90")
        Double latitude,

        @NotNull(message = "longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "longitude must be between -180 and 180")
        Double longitude,

        StationStatus status,

        @Positive(message = "samplingIntervalSec must be greater than 0")
        Integer samplingIntervalSec,

        @NotNull(message = "zoneId is required")
        Long zoneId
) {
}

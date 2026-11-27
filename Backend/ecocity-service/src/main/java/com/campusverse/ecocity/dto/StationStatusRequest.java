package com.campusverse.ecocity.dto;

import com.campusverse.ecocity.enums.StationStatus;
import jakarta.validation.constraints.NotNull;

/** Body of PATCH /api/monitoring-stations/{id}/status. */
public record StationStatusRequest(
        @NotNull(message = "status is required")
        StationStatus status
) {
}

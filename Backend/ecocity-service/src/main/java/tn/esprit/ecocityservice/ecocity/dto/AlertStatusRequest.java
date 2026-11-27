package tn.esprit.ecocityservice.ecocity.dto;

import tn.esprit.ecocityservice.ecocity.enums.AlertStatus;
import jakarta.validation.constraints.NotNull;

/** Body of PATCH /api/pollution-alerts/{id}/status: ACKNOWLEDGED or RESOLVED. */
public record AlertStatusRequest(
        @NotNull(message = "status is required")
        AlertStatus status
) {
}

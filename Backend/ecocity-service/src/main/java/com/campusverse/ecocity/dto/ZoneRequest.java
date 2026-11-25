package com.campusverse.ecocity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data sent to create or update a zone.
 * boundary is optional and written in WKT, longitude first, for example:
 * "POLYGON((10.17 36.80, 10.20 36.80, 10.20 36.83, 10.17 36.83, 10.17 36.80))"
 */
public record ZoneRequest(

        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Size(max = 100, message = "district must be at most 100 characters")
        String district,

        String boundary
) {
}

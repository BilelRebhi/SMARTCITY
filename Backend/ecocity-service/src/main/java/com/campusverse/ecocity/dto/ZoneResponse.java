package com.campusverse.ecocity.dto;

/** Zone returned by the API. boundary is in WKT format (null if not set). */
public record ZoneResponse(
        Long id,
        String name,
        String district,
        String boundary
) {
}

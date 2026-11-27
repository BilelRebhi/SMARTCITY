package com.campusverse.ecocity.mapper;

import com.campusverse.ecocity.dto.*;
import com.campusverse.ecocity.entity.*;
import com.campusverse.ecocity.enums.Indicator;
import com.campusverse.ecocity.exception.InvalidRequestException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** Converts between entities and DTOs (the API never exposes entities directly). */
public final class EcoCityMapper {

    private static final int WGS84 = 4326;

    private EcoCityMapper() {
    }

    // ------------------------------------------------------------------ Zone

    public static void updateEntity(Zone zone, ZoneRequest request) {
        zone.setName(request.name().trim());
        zone.setDistrict(request.district());
        zone.setBoundary(toPolygon(request.boundary()));
    }

    public static ZoneResponse toResponse(Zone zone) {
        return new ZoneResponse(
                zone.getId(),
                zone.getName(),
                zone.getDistrict(),
                zone.getBoundary() == null ? null : zone.getBoundary().toText());
    }

    /** Reads a WKT polygon such as "POLYGON((lng lat, lng lat, ...))". Returns null when the text is empty. */
    static Polygon toPolygon(String wkt) {
        if (wkt == null || wkt.isBlank()) {
            return null;
        }
        Geometry geometry;
        try {
            geometry = new WKTReader().read(wkt);
        } catch (ParseException e) {
            throw new InvalidRequestException("boundary is not valid WKT: " + e.getMessage());
        }
        if (!(geometry instanceof Polygon polygon) || !polygon.isValid()) {
            throw new InvalidRequestException("boundary must be a valid, closed POLYGON");
        }
        polygon.setSRID(WGS84);
        return polygon;
    }

    // ------------------------------------------------------------------ MonitoringStation

    public static void updateEntity(MonitoringStation station, StationRequest request, Zone zone) {
        station.setCode(request.code().trim());
        station.setName(request.name());
        station.setLatitude(request.latitude());
        station.setLongitude(request.longitude());
        if (request.status() != null) {
            station.setStatus(request.status());
        }
        if (request.samplingIntervalSec() != null) {
            station.setSamplingIntervalSec(request.samplingIntervalSec());
        }
        station.setZone(zone);
    }

    public static StationResponse toResponse(MonitoringStation station) {
        return new StationResponse(
                station.getId(),
                station.getCode(),
                station.getName(),
                station.getLatitude(),
                station.getLongitude(),
                station.getStatus(),
                station.getSamplingIntervalSec(),
                station.getZone().getId(),
                station.getZone().getName());
    }

    // ------------------------------------------------------------------ Measurement (reading)

    public static ReadingResponse toResponse(Measurement measurement) {
        return new ReadingResponse(
                measurement.getId(),
                measurement.getStation().getId(),
                measurement.getStation().getCode(),
                measurement.getIndicator(),
                measurement.getValue(),
                measurement.getUnit(),
                measurement.getRecordedAt(),
                measurement.getQuality());
    }

    // ------------------------------------------------------------------ Threshold

    public static void updateEntity(Threshold threshold, ThresholdRequest request) {
        threshold.setIndicator(request.indicator());
        threshold.setMaxValue(request.maxValue());
        threshold.setUnit(resolveUnit(request.indicator(), request.unit()));
        threshold.setSeverity(request.severity());
        threshold.setActive(request.active() == null || request.active());
    }

    public static ThresholdResponse toResponse(Threshold threshold) {
        return new ThresholdResponse(
                threshold.getId(),
                threshold.getIndicator(),
                threshold.getMaxValue(),
                threshold.getUnit(),
                threshold.getSeverity(),
                threshold.getActive());
    }

    // ------------------------------------------------------------------ PollutionAlert

    public static AlertResponse toResponse(PollutionAlert alert) {
        MonitoringStation station = alert.getMeasurement().getStation();
        return new AlertResponse(
                alert.getId(),
                alert.getIndicator(),
                alert.getValue(),
                alert.getThreshold().getMaxValue(),
                alert.getSeverity(),
                alert.getStatus(),
                alert.getTriggeredAt(),
                alert.getMeasurement().getId(),
                alert.getThreshold().getId(),
                station.getId(),
                station.getZone().getId());
    }

    // ------------------------------------------------------------------ EnvReport

    public static void updateEntity(EnvReport report, EnvReportRequest request, Zone zone) {
        report.setZone(zone);
        report.setPeriodStart(request.periodStart());
        report.setPeriodEnd(request.periodEnd());
        report.setAvgValue(request.avgValue());
        report.setMaxValue(request.maxValue());
    }

    public static EnvReportResponse toResponse(EnvReport report) {
        return new EnvReportResponse(
                report.getId(),
                report.getZone().getId(),
                report.getZone().getName(),
                report.getPeriodStart(),
                report.getPeriodEnd(),
                report.getAvgValue(),
                report.getMaxValue());
    }

    // ------------------------------------------------------------------ helpers

    /** Uses the indicator's unit when none is given, and rejects a unit that does not match the indicator. */
    public static String resolveUnit(Indicator indicator, String unit) {
        if (unit == null || unit.isBlank()) {
            return indicator.getUnit();
        }
        if (!unit.trim().equalsIgnoreCase(indicator.getUnit())) {
            throw new InvalidRequestException(
                    "unit '" + unit + "' is not valid for " + indicator + " (expected '" + indicator.getUnit() + "')");
        }
        return indicator.getUnit();
    }
}

package com.campusverse.ecocity.repository;

import com.campusverse.ecocity.entity.PollutionAlert;
import com.campusverse.ecocity.enums.AlertStatus;
import com.campusverse.ecocity.enums.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollutionAlertRepository extends JpaRepository<PollutionAlert, Long> {

    boolean existsByMeasurementId(Long measurementId);

    boolean existsByThresholdId(Long thresholdId);

    /** Used to keep only one OPEN alert per station and indicator. */
    boolean existsByMeasurementStationIdAndIndicatorAndStatus(Long stationId, Indicator indicator, AlertStatus status);

    long countByStatusAndMeasurementStationZoneId(AlertStatus status, Long zoneId);
}

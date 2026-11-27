package com.campusverse.ecocity.repository;

import com.campusverse.ecocity.entity.MonitoringStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoringStationRepository extends JpaRepository<MonitoringStation, Long> {

    List<MonitoringStation> findByZoneIdOrderByCode(Long zoneId);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);

    boolean existsByZoneId(Long zoneId);
}

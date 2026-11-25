package com.campusverse.ecocity.repository;

import com.campusverse.ecocity.entity.EnvReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvReportRepository extends JpaRepository<EnvReport, Long> {

    boolean existsByZoneId(Long zoneId);
}

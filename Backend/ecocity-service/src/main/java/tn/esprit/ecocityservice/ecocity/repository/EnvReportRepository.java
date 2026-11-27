package tn.esprit.ecocityservice.ecocity.repository;

import tn.esprit.ecocityservice.ecocity.entity.EnvReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvReportRepository extends JpaRepository<EnvReport, Long> {

    boolean existsByZoneId(Long zoneId);
}

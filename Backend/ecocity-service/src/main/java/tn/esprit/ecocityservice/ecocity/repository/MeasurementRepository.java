package tn.esprit.ecocityservice.ecocity.repository;

import tn.esprit.ecocityservice.ecocity.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    boolean existsByStationId(Long stationId);

    List<Measurement> findByStationZoneId(Long zoneId);
}

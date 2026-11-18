package com.smartcity.urbanincident.repository;

import com.smartcity.urbanincident.entity.Incident;
import com.smartcity.urbanincident.enums.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    List<Incident> findByCitizenId(Long citizenId);
    
    List<Incident> findByStatus(IncidentStatus status);
    
    List<Incident> findByCitizenIdAndStatus(Long citizenId, IncidentStatus status);
}

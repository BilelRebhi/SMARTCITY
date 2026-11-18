package com.smartcity.urbanincident.repository;

import com.smartcity.urbanincident.entity.ResolutionConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResolutionConfirmationRepository extends JpaRepository<ResolutionConfirmation, Long> {
    
    Optional<ResolutionConfirmation> findByIncidentId(Long incidentId);
}

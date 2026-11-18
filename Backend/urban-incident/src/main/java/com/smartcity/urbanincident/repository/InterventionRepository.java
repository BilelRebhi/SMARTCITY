package com.smartcity.urbanincident.repository;

import com.smartcity.urbanincident.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    
    Optional<Intervention> findByWorkOrderId(Long workOrderId);
    
    List<Intervention> findByTechnicianId(Long technicianId);
}

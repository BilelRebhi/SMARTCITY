package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.Intervention;

import java.util.List;
import java.util.Optional;

public interface InterventionService {
    
    Intervention createIntervention(Intervention intervention);
    
    Intervention updateIntervention(Long id, Intervention intervention);
    
    Optional<Intervention> getInterventionById(Long id);
    
    Optional<Intervention> getInterventionByWorkOrder(Long workOrderId);
    
    List<Intervention> getInterventionsByTechnician(Long technicianId);
    
    List<Intervention> getAllInterventions();
    
    void deleteIntervention(Long id);
}

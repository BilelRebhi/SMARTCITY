package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.Incident;
import com.smartcity.urbanincident.enums.IncidentStatus;

import java.util.List;
import java.util.Optional;

public interface IncidentService {
    
    Incident createIncident(Incident incident);
    
    Incident updateIncident(Long id, Incident incident);
    
    Optional<Incident> getIncidentById(Long id);
    
    List<Incident> getAllIncidents();
    
    List<Incident> getIncidentsByCitizen(Long citizenId);
    
    List<Incident> getIncidentsByStatus(IncidentStatus status);
    
    void deleteIncident(Long id);
    
    Incident updateIncidentStatus(Long id, IncidentStatus status);
}

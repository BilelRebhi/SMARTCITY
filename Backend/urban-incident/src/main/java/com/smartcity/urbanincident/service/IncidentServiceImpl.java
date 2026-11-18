package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.Incident;
import com.smartcity.urbanincident.enums.IncidentStatus;
import com.smartcity.urbanincident.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {
    
    private final IncidentRepository incidentRepository;
    
    @Override
    public Incident createIncident(Incident incident) {
        // Implementation will be added
        return null;
    }
    
    @Override
    public Incident updateIncident(Long id, Incident incident) {
        // Implementation will be added
        return null;
    }
    
    @Override
    public Optional<Incident> getIncidentById(Long id) {
        // Implementation will be added
        return Optional.empty();
    }
    
    @Override
    public List<Incident> getAllIncidents() {
        // Implementation will be added
        return null;
    }
    
    @Override
    public List<Incident> getIncidentsByCitizen(Long citizenId) {
        // Implementation will be added
        return null;
    }
    
    @Override
    public List<Incident> getIncidentsByStatus(IncidentStatus status) {
        // Implementation will be added
        return null;
    }
    
    @Override
    public void deleteIncident(Long id) {
        // Implementation will be added
    }
    
    @Override
    public Incident updateIncidentStatus(Long id, IncidentStatus status) {
        // Implementation will be added
        return null;
    }
}

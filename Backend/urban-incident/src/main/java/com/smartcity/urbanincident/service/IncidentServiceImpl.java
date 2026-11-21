package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.Incident;
import com.smartcity.urbanincident.enums.IncidentStatus;
import com.smartcity.urbanincident.exception.ResourceNotFoundException;
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
        return incidentRepository.save(incident);
    }

    @Override
    public Incident updateIncident(Long id, Incident incident) {
        if (!incidentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Incident not found with id: " + id);
        }
        incident.setId(id);
        return incidentRepository.save(incident);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Incident> getIncidentById(Long id) {
        return incidentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getIncidentsByCitizen(Long citizenId) {
        return incidentRepository.findByCitizenId(citizenId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getIncidentsByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status);
    }

    @Override
    public void deleteIncident(Long id) {
        if (!incidentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Incident not found with id: " + id);
        }
        incidentRepository.deleteById(id);
    }

    @Override
    public Incident updateIncidentStatus(Long id, IncidentStatus status) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id: " + id));
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }
}

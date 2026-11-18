package com.smartcity.urbanincident.controller;

import com.smartcity.urbanincident.entity.Incident;
import com.smartcity.urbanincident.enums.IncidentStatus;
import com.smartcity.urbanincident.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    
    private final IncidentService incidentService;
    
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        // Implementation will be added
        return null;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<List<Incident>> getIncidentsByCitizen(@PathVariable Long citizenId) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Incident>> getIncidentsByStatus(@PathVariable IncidentStatus status) {
        // Implementation will be added
        return null;
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Incident> updateIncidentStatus(@PathVariable Long id, @RequestParam IncidentStatus status) {
        // Implementation will be added
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
}

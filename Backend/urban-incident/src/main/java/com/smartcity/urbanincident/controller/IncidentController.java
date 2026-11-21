package com.smartcity.urbanincident.controller;

import com.smartcity.urbanincident.entity.Incident;
import com.smartcity.urbanincident.enums.IncidentStatus;
import com.smartcity.urbanincident.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(incidentService.createIncident(incident), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return incidentService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<List<Incident>> getIncidentsByCitizen(@PathVariable Long citizenId) {
        return ResponseEntity.ok(incidentService.getIncidentsByCitizen(citizenId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Incident>> getIncidentsByStatus(@PathVariable IncidentStatus status) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Incident> updateIncidentStatus(@PathVariable Long id, @RequestParam IncidentStatus status) {
        return ResponseEntity.ok(incidentService.updateIncidentStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}

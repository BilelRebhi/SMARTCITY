package com.smartcity.urbanincident.controller;

import com.smartcity.urbanincident.entity.Intervention;
import com.smartcity.urbanincident.service.InterventionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@RequiredArgsConstructor
public class InterventionController {
    
    private final InterventionService interventionService;
    
    @PostMapping
    public ResponseEntity<Intervention> createIntervention(@RequestBody Intervention intervention) {
        // Implementation will be added
        return null;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Intervention> updateIntervention(@PathVariable Long id, @RequestBody Intervention intervention) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Intervention> getInterventionById(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping
    public ResponseEntity<List<Intervention>> getAllInterventions() {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/work-order/{workOrderId}")
    public ResponseEntity<Intervention> getInterventionByWorkOrder(@PathVariable Long workOrderId) {
        // Implementation will be added
        return null;
    }
    
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<Intervention>> getInterventionsByTechnician(@PathVariable Long technicianId) {
        // Implementation will be added
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        // Implementation will be added
        return null;
    }
}

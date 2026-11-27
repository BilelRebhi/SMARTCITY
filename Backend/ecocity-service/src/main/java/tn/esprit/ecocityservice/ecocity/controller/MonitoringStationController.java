package tn.esprit.ecocityservice.ecocity.controller;

import tn.esprit.ecocityservice.ecocity.dto.StationRequest;
import tn.esprit.ecocityservice.ecocity.dto.StationResponse;
import tn.esprit.ecocityservice.ecocity.dto.StationStatusRequest;
import tn.esprit.ecocityservice.ecocity.service.MonitoringStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Monitoring stations", description = "Virtual stations per zone")
@RestController
@RequestMapping("/api/monitoring-stations")
@RequiredArgsConstructor
public class MonitoringStationController {

    private final MonitoringStationService stationService;

    @Operation(summary = "GET /api/monitoring-stations?zoneId=", description = "Responses: 200")
    @GetMapping
    public List<StationResponse> findAll(@RequestParam(required = false) Long zoneId) {
        return stationService.findAll(zoneId);
    }

    @Operation(summary = "GET /api/monitoring-stations/{id}", description = "Responses: 200, 404")
    @GetMapping("/{id}")
    public StationResponse findById(@PathVariable Long id) {
        return stationService.findById(id);
    }

    @Operation(summary = "POST /api/monitoring-stations", description = "Responses: 201, 400, 404 zone, 409 code exists")
    @PostMapping
    public ResponseEntity<StationResponse> create(@Valid @RequestBody StationRequest request) {
        StationResponse created = stationService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "PUT /api/monitoring-stations/{id}", description = "Responses: 200, 400, 404, 409")
    @PutMapping("/{id}")
    public StationResponse update(@PathVariable Long id, @Valid @RequestBody StationRequest request) {
        return stationService.update(id, request);
    }

    @Operation(summary = "PATCH /api/monitoring-stations/{id}/status", description = "Responses: 200, 400, 404")
    @PatchMapping("/{id}/status")
    public StationResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StationStatusRequest request) {
        return stationService.updateStatus(id, request.status());
    }

    @Operation(summary = "DELETE /api/monitoring-stations/{id}", description = "Responses: 204, 404, 409 has readings")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

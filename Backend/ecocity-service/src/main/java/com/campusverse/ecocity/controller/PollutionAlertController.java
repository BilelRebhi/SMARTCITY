package com.campusverse.ecocity.controller;

import com.campusverse.ecocity.dto.AlertRequest;
import com.campusverse.ecocity.dto.AlertResponse;
import com.campusverse.ecocity.dto.AlertStatusRequest;
import com.campusverse.ecocity.enums.AlertStatus;
import com.campusverse.ecocity.service.PollutionAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Pollution alerts", description = "Alerts raised when a threshold is exceeded")
@RestController
@RequestMapping("/api/pollution-alerts")
@RequiredArgsConstructor
public class PollutionAlertController {

    private final PollutionAlertService alertService;

    @Operation(summary = "GET /api/pollution-alerts?status=&zoneId=", description = "Responses: 200")
    @GetMapping
    public List<AlertResponse> findAll(@RequestParam(required = false) AlertStatus status,
                                       @RequestParam(required = false) Long zoneId) {
        return alertService.findAll(status, zoneId);
    }

    @Operation(summary = "GET /api/pollution-alerts/{id}", description = "Responses: 200, 404")
    @GetMapping("/{id}")
    public AlertResponse findById(@PathVariable Long id) {
        return alertService.findById(id);
    }

    @Operation(summary = "POST /api/pollution-alerts", description = "Responses: 201, 400 value under threshold, 404, 409 open alert exists")
    @PostMapping
    public ResponseEntity<AlertResponse> create(@Valid @RequestBody AlertRequest request) {
        AlertResponse created = alertService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "PATCH /api/pollution-alerts/{id}/status", description = "Responses: 200, 400, 404, 409 invalid transition")
    @PatchMapping("/{id}/status")
    public AlertResponse updateStatus(@PathVariable Long id, @Valid @RequestBody AlertStatusRequest request) {
        return alertService.updateStatus(id, request.status());
    }

    @Operation(summary = "DELETE /api/pollution-alerts/{id}", description = "Responses: 204, 404")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

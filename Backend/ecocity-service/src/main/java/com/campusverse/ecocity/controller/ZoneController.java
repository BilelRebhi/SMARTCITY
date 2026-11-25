package com.campusverse.ecocity.controller;

import com.campusverse.ecocity.dto.ZoneConditionsResponse;
import com.campusverse.ecocity.dto.ZoneRequest;
import com.campusverse.ecocity.dto.ZoneResponse;
import com.campusverse.ecocity.service.ZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Zones", description = "City neighborhoods")
@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @Operation(summary = "GET /api/zones", description = "Responses: 200")
    @GetMapping
    public List<ZoneResponse> findAll() {
        return zoneService.findAll();
    }

    @Operation(summary = "GET /api/zones/my-zone/conditions",
            description = "Conditions in the citizen's zone (calls MS1). Responses: 200, 404, 503 MS1 unavailable")
    @GetMapping("/my-zone/conditions")
    public ZoneConditionsResponse myZoneConditions(@RequestHeader("X-Citizen-Id") Long citizenId) {
        return zoneService.getMyZoneConditions(citizenId);
    }

    @Operation(summary = "GET /api/zones/{id}", description = "Responses: 200, 404")
    @GetMapping("/{id}")
    public ZoneResponse findById(@PathVariable Long id) {
        return zoneService.findById(id);
    }

    @Operation(summary = "POST /api/zones", description = "Responses: 201, 400, 409 name exists")
    @PostMapping
    public ResponseEntity<ZoneResponse> create(@Valid @RequestBody ZoneRequest request) {
        ZoneResponse created = zoneService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "PUT /api/zones/{id}", description = "Responses: 200, 400, 404, 409")
    @PutMapping("/{id}")
    public ZoneResponse update(@PathVariable Long id, @Valid @RequestBody ZoneRequest request) {
        return zoneService.update(id, request);
    }

    @Operation(summary = "DELETE /api/zones/{id}", description = "Responses: 204, 404, 409 still has stations or reports")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

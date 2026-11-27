package com.campusverse.ecocity.controller;

import com.campusverse.ecocity.dto.ThresholdRequest;
import com.campusverse.ecocity.dto.ThresholdResponse;
import com.campusverse.ecocity.enums.Indicator;
import com.campusverse.ecocity.service.ThresholdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Thresholds", description = "Maximum allowed value per indicator")
@RestController
@RequestMapping("/api/thresholds")
@RequiredArgsConstructor
public class ThresholdController {

    private final ThresholdService thresholdService;

    @Operation(summary = "GET /api/thresholds?indicator=&active=", description = "Responses: 200")
    @GetMapping
    public List<ThresholdResponse> findAll(@RequestParam(required = false) Indicator indicator,
                                           @RequestParam(required = false) Boolean active) {
        return thresholdService.findAll(indicator, active);
    }

    @Operation(summary = "GET /api/thresholds/{id}", description = "Responses: 200, 404")
    @GetMapping("/{id}")
    public ThresholdResponse findById(@PathVariable Long id) {
        return thresholdService.findById(id);
    }

    @Operation(summary = "POST /api/thresholds", description = "Responses: 201, 400, 409 active threshold exists")
    @PostMapping
    public ResponseEntity<ThresholdResponse> create(@Valid @RequestBody ThresholdRequest request) {
        ThresholdResponse created = thresholdService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "PUT /api/thresholds/{id}", description = "Responses: 200, 400, 404, 409")
    @PutMapping("/{id}")
    public ThresholdResponse update(@PathVariable Long id, @Valid @RequestBody ThresholdRequest request) {
        return thresholdService.update(id, request);
    }

    @Operation(summary = "DELETE /api/thresholds/{id}", description = "Responses: 204, 404, 409 used by alerts")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        thresholdService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package tn.esprit.ecocityservice.ecocity.controller;

import tn.esprit.ecocityservice.ecocity.dto.EnvReportRequest;
import tn.esprit.ecocityservice.ecocity.dto.EnvReportResponse;
import tn.esprit.ecocityservice.ecocity.service.EnvReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Environmental reports", description = "Average and maximum values per zone and period")
@RestController
@RequestMapping("/api/environmental-reports")
@RequiredArgsConstructor
public class EnvReportController {

    private final EnvReportService reportService;

    @Operation(summary = "GET /api/environmental-reports?zoneId=&from=&to=", description = "Responses: 200, 400")
    @GetMapping
    public List<EnvReportResponse> findAll(
            @RequestParam(required = false) Long zoneId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.findAll(zoneId, from, to);
    }

    @Operation(summary = "GET /api/environmental-reports/{id}", description = "Responses: 200, 404")
    @GetMapping("/{id}")
    public EnvReportResponse findById(@PathVariable Long id) {
        return reportService.findById(id);
    }

    @Operation(summary = "POST /api/environmental-reports", description = "Responses: 201, 400, 404 zone")
    @PostMapping
    public ResponseEntity<EnvReportResponse> create(@Valid @RequestBody EnvReportRequest request) {
        EnvReportResponse created = reportService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "PUT /api/environmental-reports/{id}", description = "Responses: 200, 400, 404")
    @PutMapping("/{id}")
    public EnvReportResponse update(@PathVariable Long id, @Valid @RequestBody EnvReportRequest request) {
        return reportService.update(id, request);
    }

    @Operation(summary = "DELETE /api/environmental-reports/{id}", description = "Responses: 204, 404")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

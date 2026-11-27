package tn.esprit.ecocityservice.ecocity.controller;

import tn.esprit.ecocityservice.ecocity.dto.ReadingRequest;
import tn.esprit.ecocityservice.ecocity.dto.ReadingResponse;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import tn.esprit.ecocityservice.ecocity.service.ReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Readings", description = "Air quality and noise measurements")
@RestController
@RequestMapping("/api/readings")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;

    @Operation(summary = "GET /api/readings?stationId=&indicator=&from=&to=", description = "Responses: 200, 400")
    @GetMapping
    public List<ReadingResponse> findAll(
            @RequestParam(required = false) Long stationId,
            @RequestParam(required = false) Indicator indicator,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return readingService.findAll(stationId, indicator, from, to);
    }

    @Operation(summary = "GET /api/readings/{id}", description = "Responses: 200, 404")
    @GetMapping("/{id}")
    public ReadingResponse findById(@PathVariable Long id) {
        return readingService.findById(id);
    }

    @Operation(summary = "POST /api/readings", description = "Responses: 201, 400 invalid value/unit, 404 station")
    @PostMapping
    public ResponseEntity<ReadingResponse> create(@Valid @RequestBody ReadingRequest request) {
        ReadingResponse created = readingService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "PUT /api/readings/{id}", description = "Responses: 200, 400, 404")
    @PutMapping("/{id}")
    public ReadingResponse update(@PathVariable Long id, @Valid @RequestBody ReadingRequest request) {
        return readingService.update(id, request);
    }

    @Operation(summary = "DELETE /api/readings/{id}", description = "Responses: 204, 404, 409 has an alert")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        readingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.StationRequest;
import tn.esprit.ecocityservice.ecocity.dto.StationResponse;
import tn.esprit.ecocityservice.ecocity.enums.StationStatus;

import java.util.List;

public interface MonitoringStationService {

    List<StationResponse> findAll(Long zoneId);

    StationResponse findById(Long id);

    StationResponse create(StationRequest request);

    StationResponse update(Long id, StationRequest request);

    StationResponse updateStatus(Long id, StationStatus status);

    void delete(Long id);
}

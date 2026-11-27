package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.StationRequest;
import com.campusverse.ecocity.dto.StationResponse;
import com.campusverse.ecocity.enums.StationStatus;

import java.util.List;

public interface MonitoringStationService {

    List<StationResponse> findAll(Long zoneId);

    StationResponse findById(Long id);

    StationResponse create(StationRequest request);

    StationResponse update(Long id, StationRequest request);

    StationResponse updateStatus(Long id, StationStatus status);

    void delete(Long id);
}

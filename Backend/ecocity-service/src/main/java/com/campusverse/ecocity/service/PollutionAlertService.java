package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.AlertRequest;
import com.campusverse.ecocity.dto.AlertResponse;
import com.campusverse.ecocity.enums.AlertStatus;

import java.util.List;

public interface PollutionAlertService {

    List<AlertResponse> findAll(AlertStatus status, Long zoneId);

    AlertResponse findById(Long id);

    AlertResponse create(AlertRequest request);

    AlertResponse updateStatus(Long id, AlertStatus status);

    void delete(Long id);
}

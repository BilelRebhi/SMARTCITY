package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.AlertRequest;
import tn.esprit.ecocityservice.ecocity.dto.AlertResponse;
import tn.esprit.ecocityservice.ecocity.enums.AlertStatus;

import java.util.List;

public interface PollutionAlertService {

    List<AlertResponse> findAll(AlertStatus status, Long zoneId);

    AlertResponse findById(Long id);

    AlertResponse create(AlertRequest request);

    AlertResponse updateStatus(Long id, AlertStatus status);

    void delete(Long id);
}

package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.AlertRequest;
import tn.esprit.ecocityservice.ecocity.dto.AlertResponse;
import tn.esprit.ecocityservice.ecocity.entity.Measurement;
import tn.esprit.ecocityservice.ecocity.entity.PollutionAlert;
import tn.esprit.ecocityservice.ecocity.entity.Threshold;
import tn.esprit.ecocityservice.ecocity.enums.AlertStatus;
import tn.esprit.ecocityservice.ecocity.event.AlertEventPublisher;
import tn.esprit.ecocityservice.ecocity.exception.BusinessRuleException;
import tn.esprit.ecocityservice.ecocity.exception.InvalidRequestException;
import tn.esprit.ecocityservice.ecocity.exception.ResourceNotFoundException;
import tn.esprit.ecocityservice.ecocity.mapper.AlertMapper;
import tn.esprit.ecocityservice.ecocity.repository.MeasurementRepository;
import tn.esprit.ecocityservice.ecocity.repository.PollutionAlertRepository;
import tn.esprit.ecocityservice.ecocity.repository.ThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PollutionAlertServiceImpl implements PollutionAlertService {

    private final PollutionAlertRepository alertRepository;
    private final MeasurementRepository measurementRepository;
    private final ThresholdRepository thresholdRepository;
    private final AlertEventPublisher eventPublisher;
    private final AlertMapper alertMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AlertResponse> findAll(AlertStatus status, Long zoneId) {
        return alertRepository.findAll(Sort.by(Sort.Direction.DESC, "triggeredAt")).stream()
                .filter(a -> status == null || a.getStatus() == status)
                .filter(a -> zoneId == null || a.getMeasurement().getStation().getZone().getId().equals(zoneId))
                .map(alertMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponse findById(Long id) {
        return alertMapper.toResponse(getAlert(id));
    }

    @Override
    public AlertResponse create(AlertRequest request) {
        Measurement measurement = measurementRepository.findById(request.measurementId())
                .orElseThrow(() -> new ResourceNotFoundException("Reading " + request.measurementId() + " not found"));
        Threshold threshold = thresholdRepository.findById(request.thresholdId())
                .orElseThrow(() -> new ResourceNotFoundException("Threshold " + request.thresholdId() + " not found"));

        if (measurement.getIndicator() != threshold.getIndicator()) {
            throw new InvalidRequestException("The reading is " + measurement.getIndicator()
                    + " but the threshold is for " + threshold.getIndicator());
        }
        if (measurement.getValue() <= threshold.getMaxValue()) {
            throw new InvalidRequestException("The reading value " + measurement.getValue()
                    + " does not exceed the threshold " + threshold.getMaxValue());
        }
        if (alertRepository.existsByMeasurementId(measurement.getId())) {
            throw new BusinessRuleException("Reading " + measurement.getId() + " already has an alert");
        }
        if (alertRepository.existsByMeasurementStationIdAndIndicatorAndStatus(
                measurement.getStation().getId(), measurement.getIndicator(), AlertStatus.OPEN)) {
            throw new BusinessRuleException("Station " + measurement.getStation().getId()
                    + " already has an OPEN alert for " + measurement.getIndicator());
        }

        PollutionAlert alert = new PollutionAlert();
        alert.setMeasurement(measurement);
        alert.setThreshold(threshold);
        alert.setIndicator(measurement.getIndicator());
        alert.setValue(measurement.getValue());
        alert.setSeverity(threshold.getSeverity());
        alert.setTriggeredAt(LocalDateTime.now());
        alertRepository.save(alert);
        eventPublisher.alertRaised(alert);
        return alertMapper.toResponse(alert);
    }

    @Override
    public AlertResponse updateStatus(Long id, AlertStatus status) {
        PollutionAlert alert = getAlert(id);
        boolean changed = switch (status) {
            case ACKNOWLEDGED -> alert.acknowledge();
            case RESOLVED -> alert.resolve();
            case OPEN -> false;
        };
        if (!changed) {
            throw new BusinessRuleException("Alert " + id + " cannot go from " + alert.getStatus() + " to " + status
                    + " (allowed: OPEN -> ACKNOWLEDGED -> RESOLVED, or OPEN -> RESOLVED)");
        }
        if (status == AlertStatus.RESOLVED) {
            eventPublisher.alertResolved(alert);
        }
        return alertMapper.toResponse(alert);
    }

    @Override
    public void delete(Long id) {
        alertRepository.delete(getAlert(id));
    }

    private PollutionAlert getAlert(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pollution alert " + id + " not found"));
    }
}

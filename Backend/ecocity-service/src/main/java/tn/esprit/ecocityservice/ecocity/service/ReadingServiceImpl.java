package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.ReadingRequest;
import tn.esprit.ecocityservice.ecocity.dto.ReadingResponse;
import tn.esprit.ecocityservice.ecocity.entity.Measurement;
import tn.esprit.ecocityservice.ecocity.entity.MonitoringStation;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;
import tn.esprit.ecocityservice.ecocity.enums.MeasurementQuality;
import tn.esprit.ecocityservice.ecocity.exception.BusinessRuleException;
import tn.esprit.ecocityservice.ecocity.exception.InvalidRequestException;
import tn.esprit.ecocityservice.ecocity.exception.ResourceNotFoundException;
import tn.esprit.ecocityservice.ecocity.mapper.ReadingMapper;
import tn.esprit.ecocityservice.ecocity.mapper.MapperHelper;
import tn.esprit.ecocityservice.ecocity.repository.MeasurementRepository;
import tn.esprit.ecocityservice.ecocity.repository.MonitoringStationRepository;
import tn.esprit.ecocityservice.ecocity.repository.PollutionAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingServiceImpl implements ReadingService {

    private final MeasurementRepository measurementRepository;
    private final MonitoringStationRepository stationRepository;
    private final PollutionAlertRepository alertRepository;
    private final ReadingMapper readingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReadingResponse> findAll(Long stationId, Indicator indicator, LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new InvalidRequestException("'from' must be before 'to'");
        }
        return measurementRepository.findAll(Sort.by(Sort.Direction.DESC, "recordedAt")).stream()
                .filter(m -> stationId == null || m.getStation().getId().equals(stationId))
                .filter(m -> indicator == null || m.getIndicator() == indicator)
                .filter(m -> from == null || !m.getRecordedAt().isBefore(from))
                .filter(m -> to == null || !m.getRecordedAt().isAfter(to))
                .map(readingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReadingResponse findById(Long id) {
        return readingMapper.toResponse(getMeasurement(id));
    }

    @Override
    public ReadingResponse create(ReadingRequest request) {
        Measurement measurement = new Measurement();
        apply(measurement, request);
        return readingMapper.toResponse(measurementRepository.save(measurement));
    }

    @Override
    public ReadingResponse update(Long id, ReadingRequest request) {
        Measurement measurement = getMeasurement(id);
        apply(measurement, request);
        return readingMapper.toResponse(measurement);
    }

    @Override
    public void delete(Long id) {
        Measurement measurement = getMeasurement(id);
        if (alertRepository.existsByMeasurementId(id)) {
            throw new BusinessRuleException("Reading " + id + " triggered a pollution alert; delete the alert first");
        }
        measurementRepository.delete(measurement);
    }

    private void apply(Measurement measurement, ReadingRequest request) {
        MonitoringStation station = stationRepository.findById(request.stationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monitoring station " + request.stationId() + " not found"));
        measurement.setStation(station);
        measurement.setIndicator(request.indicator());
        measurement.setValue(request.value());
        measurement.setUnit(MapperHelper.resolveUnit(request.indicator(), request.unit()));
        measurement.setRecordedAt(request.recordedAt() != null ? request.recordedAt() : LocalDateTime.now());
        measurement.setQuality(request.quality() != null ? request.quality() : MeasurementQuality.VALID);
    }

    private Measurement getMeasurement(Long id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reading " + id + " not found"));
    }
}

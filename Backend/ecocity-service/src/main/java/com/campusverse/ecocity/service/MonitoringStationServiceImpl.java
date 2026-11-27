package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.StationRequest;
import com.campusverse.ecocity.dto.StationResponse;
import com.campusverse.ecocity.entity.MonitoringStation;
import com.campusverse.ecocity.entity.Zone;
import com.campusverse.ecocity.enums.StationStatus;
import com.campusverse.ecocity.exception.BusinessRuleException;
import com.campusverse.ecocity.exception.ResourceNotFoundException;
import com.campusverse.ecocity.mapper.EcoCityMapper;
import com.campusverse.ecocity.repository.MeasurementRepository;
import com.campusverse.ecocity.repository.MonitoringStationRepository;
import com.campusverse.ecocity.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MonitoringStationServiceImpl implements MonitoringStationService {

    private final MonitoringStationRepository stationRepository;
    private final ZoneRepository zoneRepository;
    private final MeasurementRepository measurementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<StationResponse> findAll(Long zoneId) {
        List<MonitoringStation> stations = zoneId == null
                ? stationRepository.findAll(Sort.by("code"))
                : stationRepository.findByZoneIdOrderByCode(zoneId);
        return stations.stream().map(EcoCityMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StationResponse findById(Long id) {
        return EcoCityMapper.toResponse(getStation(id));
    }

    @Override
    public StationResponse create(StationRequest request) {
        Zone zone = getZone(request.zoneId());
        if (stationRepository.existsByCodeIgnoreCase(request.code().trim())) {
            throw new BusinessRuleException("A station with code '" + request.code() + "' already exists");
        }
        MonitoringStation station = new MonitoringStation();
        EcoCityMapper.updateEntity(station, request, zone);
        return EcoCityMapper.toResponse(stationRepository.save(station));
    }

    @Override
    public StationResponse update(Long id, StationRequest request) {
        MonitoringStation station = getStation(id);
        Zone zone = getZone(request.zoneId());
        if (stationRepository.existsByCodeIgnoreCaseAndIdNot(request.code().trim(), id)) {
            throw new BusinessRuleException("A station with code '" + request.code() + "' already exists");
        }
        EcoCityMapper.updateEntity(station, request, zone);
        return EcoCityMapper.toResponse(station);
    }

    @Override
    public StationResponse updateStatus(Long id, StationStatus status) {
        MonitoringStation station = getStation(id);
        station.setStatus(status);
        return EcoCityMapper.toResponse(station);
    }

    @Override
    public void delete(Long id) {
        MonitoringStation station = getStation(id);
        if (measurementRepository.existsByStationId(id)) {
            throw new BusinessRuleException(
                    "Station " + id + " has recorded readings; set its status to INACTIVE instead of deleting it");
        }
        stationRepository.delete(station);
    }

    private MonitoringStation getStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoring station " + id + " not found"));
    }

    private Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + id + " not found"));
    }
}

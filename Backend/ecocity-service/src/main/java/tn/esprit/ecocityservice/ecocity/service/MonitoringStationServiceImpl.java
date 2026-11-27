package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.StationRequest;
import tn.esprit.ecocityservice.ecocity.dto.StationResponse;
import tn.esprit.ecocityservice.ecocity.entity.MonitoringStation;
import tn.esprit.ecocityservice.ecocity.entity.Zone;
import tn.esprit.ecocityservice.ecocity.enums.StationStatus;
import tn.esprit.ecocityservice.ecocity.exception.BusinessRuleException;
import tn.esprit.ecocityservice.ecocity.exception.ResourceNotFoundException;
import tn.esprit.ecocityservice.ecocity.mapper.StationMapper;
import tn.esprit.ecocityservice.ecocity.repository.MeasurementRepository;
import tn.esprit.ecocityservice.ecocity.repository.MonitoringStationRepository;
import tn.esprit.ecocityservice.ecocity.repository.ZoneRepository;
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
    private final StationMapper stationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<StationResponse> findAll(Long zoneId) {
        List<MonitoringStation> stations = zoneId == null
                ? stationRepository.findAll(Sort.by("code"))
                : stationRepository.findByZoneIdOrderByCode(zoneId);
        return stations.stream().map(stationMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StationResponse findById(Long id) {
        return stationMapper.toResponse(getStation(id));
    }

    @Override
    public StationResponse create(StationRequest request) {
        Zone zone = getZone(request.zoneId());
        if (stationRepository.existsByCodeIgnoreCase(request.code().trim())) {
            throw new BusinessRuleException("A station with code '" + request.code() + "' already exists");
        }
        MonitoringStation station = new MonitoringStation();
        stationMapper.updateEntity(request, zone, station);
        return stationMapper.toResponse(stationRepository.save(station));
    }

    @Override
    public StationResponse update(Long id, StationRequest request) {
        MonitoringStation station = getStation(id);
        Zone zone = getZone(request.zoneId());
        if (stationRepository.existsByCodeIgnoreCaseAndIdNot(request.code().trim(), id)) {
            throw new BusinessRuleException("A station with code '" + request.code() + "' already exists");
        }
        stationMapper.updateEntity(request, zone, station);
        return stationMapper.toResponse(station);
    }

    @Override
    public StationResponse updateStatus(Long id, StationStatus status) {
        MonitoringStation station = getStation(id);
        station.setStatus(status);
        return stationMapper.toResponse(station);
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

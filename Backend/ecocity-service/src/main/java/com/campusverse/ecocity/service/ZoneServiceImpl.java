package com.campusverse.ecocity.service;

import com.campusverse.ecocity.client.CitizenClient;
import com.campusverse.ecocity.client.dto.CitizenDTO;
import com.campusverse.ecocity.dto.ZoneConditionsResponse;
import com.campusverse.ecocity.dto.ZoneRequest;
import com.campusverse.ecocity.dto.ZoneResponse;
import com.campusverse.ecocity.entity.Measurement;
import com.campusverse.ecocity.entity.Zone;
import com.campusverse.ecocity.enums.AlertStatus;
import com.campusverse.ecocity.exception.BusinessRuleException;
import com.campusverse.ecocity.exception.ExternalServiceException;
import com.campusverse.ecocity.exception.ResourceNotFoundException;
import com.campusverse.ecocity.mapper.EcoCityMapper;
import com.campusverse.ecocity.repository.EnvReportRepository;
import com.campusverse.ecocity.repository.MeasurementRepository;
import com.campusverse.ecocity.repository.MonitoringStationRepository;
import com.campusverse.ecocity.repository.PollutionAlertRepository;
import com.campusverse.ecocity.repository.ZoneRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final MonitoringStationRepository stationRepository;
    private final EnvReportRepository reportRepository;
    private final MeasurementRepository measurementRepository;
    private final PollutionAlertRepository alertRepository;
    private final CitizenClient citizenClient;

    @Override
    @Transactional(readOnly = true)
    public List<ZoneResponse> findAll() {
        return zoneRepository.findAll(Sort.by("name")).stream()
                .map(EcoCityMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ZoneResponse findById(Long id) {
        return EcoCityMapper.toResponse(getZone(id));
    }

    @Override
    public ZoneResponse create(ZoneRequest request) {
        if (zoneRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new BusinessRuleException("A zone named '" + request.name() + "' already exists");
        }
        Zone zone = new Zone();
        EcoCityMapper.updateEntity(zone, request);
        return EcoCityMapper.toResponse(zoneRepository.save(zone));
    }

    @Override
    public ZoneResponse update(Long id, ZoneRequest request) {
        Zone zone = getZone(id);
        if (zoneRepository.existsByNameIgnoreCaseAndIdNot(request.name().trim(), id)) {
            throw new BusinessRuleException("A zone named '" + request.name() + "' already exists");
        }
        EcoCityMapper.updateEntity(zone, request);
        return EcoCityMapper.toResponse(zone);
    }

    @Override
    public void delete(Long id) {
        Zone zone = getZone(id);
        if (stationRepository.existsByZoneId(id)) {
            throw new BusinessRuleException("Zone " + id + " still has monitoring stations; delete or move them first");
        }
        if (reportRepository.existsByZoneId(id)) {
            throw new BusinessRuleException("Zone " + id + " still has environmental reports; delete them first");
        }
        zoneRepository.delete(zone);
    }

    @Override
    @Transactional(readOnly = true)
    public ZoneConditionsResponse getMyZoneConditions(Long citizenId) {
        CitizenDTO citizen;
        try {
            citizen = citizenClient.getCitizen(citizenId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Citizen " + citizenId + " not found in MS1");
        } catch (FeignException e) {
            throw new ExternalServiceException("Citizen service (MS1) is unavailable, try again later");
        }
        if (citizen == null || citizen.neighborhood() == null) {
            throw new ResourceNotFoundException("Citizen " + citizenId + " has no neighborhood");
        }
        Zone zone = zoneRepository.findByNameIgnoreCase(citizen.neighborhood().trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No zone matches the neighborhood '" + citizen.neighborhood() + "'"));

        // latest reading of each indicator among the zone's stations
        Collection<Measurement> latest = measurementRepository.findByStationZoneId(zone.getId()).stream()
                .collect(Collectors.toMap(Measurement::getIndicator, m -> m,
                        (a, b) -> a.getRecordedAt().isAfter(b.getRecordedAt()) ? a : b))
                .values();
        long openAlerts = alertRepository.countByStatusAndMeasurementStationZoneId(AlertStatus.OPEN, zone.getId());

        return new ZoneConditionsResponse(zone.getId(), zone.getName(),
                latest.stream().map(EcoCityMapper::toResponse).toList(), openAlerts);
    }

    private Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + id + " not found"));
    }
}

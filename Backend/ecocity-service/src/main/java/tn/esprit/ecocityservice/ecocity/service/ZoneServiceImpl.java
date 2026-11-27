package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.client.CitizenClient;
import tn.esprit.ecocityservice.ecocity.client.dto.CitizenDTO;
import tn.esprit.ecocityservice.ecocity.dto.ZoneConditionsResponse;
import tn.esprit.ecocityservice.ecocity.dto.ZoneRequest;
import tn.esprit.ecocityservice.ecocity.dto.ZoneResponse;
import tn.esprit.ecocityservice.ecocity.entity.Measurement;
import tn.esprit.ecocityservice.ecocity.entity.Zone;
import tn.esprit.ecocityservice.ecocity.enums.AlertStatus;
import tn.esprit.ecocityservice.ecocity.exception.BusinessRuleException;
import tn.esprit.ecocityservice.ecocity.exception.ExternalServiceException;
import tn.esprit.ecocityservice.ecocity.exception.ResourceNotFoundException;
import tn.esprit.ecocityservice.ecocity.mapper.ZoneMapper;
import tn.esprit.ecocityservice.ecocity.mapper.ReadingMapper;
import tn.esprit.ecocityservice.ecocity.repository.EnvReportRepository;
import tn.esprit.ecocityservice.ecocity.repository.MeasurementRepository;
import tn.esprit.ecocityservice.ecocity.repository.MonitoringStationRepository;
import tn.esprit.ecocityservice.ecocity.repository.PollutionAlertRepository;
import tn.esprit.ecocityservice.ecocity.repository.ZoneRepository;
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
    private final ZoneMapper zoneMapper;
    private final ReadingMapper readingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ZoneResponse> findAll() {
        return zoneRepository.findAll(Sort.by("name")).stream()
                .map(zoneMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ZoneResponse findById(Long id) {
        return zoneMapper.toResponse(getZone(id));
    }

    @Override
    public ZoneResponse create(ZoneRequest request) {
        if (zoneRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new BusinessRuleException("A zone named '" + request.name() + "' already exists");
        }
        Zone zone = new Zone();
        zoneMapper.updateEntity(request, zone);
        return zoneMapper.toResponse(zoneRepository.save(zone));
    }

    @Override
    public ZoneResponse update(Long id, ZoneRequest request) {
        Zone zone = getZone(id);
        if (zoneRepository.existsByNameIgnoreCaseAndIdNot(request.name().trim(), id)) {
            throw new BusinessRuleException("A zone named '" + request.name() + "' already exists");
        }
        zoneMapper.updateEntity(request, zone);
        return zoneMapper.toResponse(zone);
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
                latest.stream().map(readingMapper::toResponse).toList(), openAlerts);
    }

    private Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + id + " not found"));
    }
}

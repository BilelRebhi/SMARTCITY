package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.ZoneRequest;
import com.campusverse.ecocity.dto.ZoneResponse;
import com.campusverse.ecocity.entity.Zone;
import com.campusverse.ecocity.exception.BusinessRuleException;
import com.campusverse.ecocity.exception.ResourceNotFoundException;
import com.campusverse.ecocity.mapper.EcoCityMapper;
import com.campusverse.ecocity.repository.EnvReportRepository;
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
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final MonitoringStationRepository stationRepository;
    private final EnvReportRepository reportRepository;

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

    private Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + id + " not found"));
    }
}

package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.EnvReportRequest;
import tn.esprit.ecocityservice.ecocity.dto.EnvReportResponse;
import tn.esprit.ecocityservice.ecocity.entity.EnvReport;
import tn.esprit.ecocityservice.ecocity.entity.Zone;
import tn.esprit.ecocityservice.ecocity.exception.InvalidRequestException;
import tn.esprit.ecocityservice.ecocity.exception.ResourceNotFoundException;
import tn.esprit.ecocityservice.ecocity.mapper.EnvReportMapper;
import tn.esprit.ecocityservice.ecocity.repository.EnvReportRepository;
import tn.esprit.ecocityservice.ecocity.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnvReportServiceImpl implements EnvReportService {

    private final EnvReportRepository reportRepository;
    private final ZoneRepository zoneRepository;
    private final EnvReportMapper reportMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnvReportResponse> findAll(Long zoneId, LocalDate from, LocalDate to) {
        checkPeriod(from, to);
        return reportRepository.findAll(Sort.by(Sort.Direction.DESC, "periodStart")).stream()
                .filter(r -> zoneId == null || r.getZone().getId().equals(zoneId))
                .filter(r -> from == null || !r.getPeriodStart().isBefore(from))
                .filter(r -> to == null || !r.getPeriodEnd().isAfter(to))
                .map(reportMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EnvReportResponse findById(Long id) {
        return reportMapper.toResponse(getReport(id));
    }

    @Override
    public EnvReportResponse create(EnvReportRequest request) {
        EnvReport report = new EnvReport();
        apply(report, request);
        return reportMapper.toResponse(reportRepository.save(report));
    }

    @Override
    public EnvReportResponse update(Long id, EnvReportRequest request) {
        EnvReport report = getReport(id);
        apply(report, request);
        return reportMapper.toResponse(report);
    }

    @Override
    public void delete(Long id) {
        reportRepository.delete(getReport(id));
    }

    private void apply(EnvReport report, EnvReportRequest request) {
        checkPeriod(request.periodStart(), request.periodEnd());
        if (request.avgValue() != null && request.maxValue() != null && request.avgValue() > request.maxValue()) {
            throw new InvalidRequestException("avgValue cannot be greater than maxValue");
        }
        Zone zone = zoneRepository.findById(request.zoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + request.zoneId() + " not found"));
        reportMapper.updateEntity(request, zone, report);
    }

    private void checkPeriod(LocalDate start, LocalDate end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new InvalidRequestException("The period start must be on or before the period end");
        }
    }

    private EnvReport getReport(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Environmental report " + id + " not found"));
    }
}

package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.ThresholdRequest;
import com.campusverse.ecocity.dto.ThresholdResponse;
import com.campusverse.ecocity.entity.Threshold;
import com.campusverse.ecocity.enums.Indicator;
import com.campusverse.ecocity.exception.BusinessRuleException;
import com.campusverse.ecocity.exception.ResourceNotFoundException;
import com.campusverse.ecocity.mapper.EcoCityMapper;
import com.campusverse.ecocity.repository.PollutionAlertRepository;
import com.campusverse.ecocity.repository.ThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ThresholdServiceImpl implements ThresholdService {

    private final ThresholdRepository thresholdRepository;
    private final PollutionAlertRepository alertRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ThresholdResponse> findAll(Indicator indicator, Boolean active) {
        List<Threshold> thresholds;
        if (indicator != null && active != null) {
            thresholds = thresholdRepository.findByIndicatorAndActiveOrderById(indicator, active);
        } else if (indicator != null) {
            thresholds = thresholdRepository.findByIndicatorOrderById(indicator);
        } else if (active != null) {
            thresholds = thresholdRepository.findByActiveOrderById(active);
        } else {
            thresholds = thresholdRepository.findAll(Sort.by("id"));
        }
        return thresholds.stream().map(EcoCityMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ThresholdResponse findById(Long id) {
        return EcoCityMapper.toResponse(getThreshold(id));
    }

    @Override
    public ThresholdResponse create(ThresholdRequest request) {
        Threshold threshold = new Threshold();
        EcoCityMapper.updateEntity(threshold, request);
        if (threshold.getActive() && thresholdRepository.existsByIndicatorAndActiveTrue(threshold.getIndicator())) {
            throw activeExists(threshold.getIndicator());
        }
        return EcoCityMapper.toResponse(thresholdRepository.save(threshold));
    }

    @Override
    public ThresholdResponse update(Long id, ThresholdRequest request) {
        Threshold threshold = getThreshold(id);
        EcoCityMapper.updateEntity(threshold, request);
        if (threshold.getActive()
                && thresholdRepository.existsByIndicatorAndActiveTrueAndIdNot(threshold.getIndicator(), id)) {
            throw activeExists(threshold.getIndicator());
        }
        return EcoCityMapper.toResponse(threshold);
    }

    @Override
    public void delete(Long id) {
        Threshold threshold = getThreshold(id);
        if (alertRepository.existsByThresholdId(id)) {
            throw new BusinessRuleException(
                    "Threshold " + id + " is used by pollution alerts; set active=false instead of deleting it");
        }
        thresholdRepository.delete(threshold);
    }

    private BusinessRuleException activeExists(Indicator indicator) {
        return new BusinessRuleException("An active threshold already exists for " + indicator
                + "; deactivate it first (only one active threshold per indicator)");
    }

    private Threshold getThreshold(Long id) {
        return thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Threshold " + id + " not found"));
    }
}

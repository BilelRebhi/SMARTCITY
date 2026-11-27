package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.ThresholdRequest;
import com.campusverse.ecocity.dto.ThresholdResponse;
import com.campusverse.ecocity.enums.Indicator;

import java.util.List;

public interface ThresholdService {

    List<ThresholdResponse> findAll(Indicator indicator, Boolean active);

    ThresholdResponse findById(Long id);

    ThresholdResponse create(ThresholdRequest request);

    ThresholdResponse update(Long id, ThresholdRequest request);

    void delete(Long id);
}

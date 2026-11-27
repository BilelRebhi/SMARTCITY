package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.ThresholdRequest;
import tn.esprit.ecocityservice.ecocity.dto.ThresholdResponse;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;

import java.util.List;

public interface ThresholdService {

    List<ThresholdResponse> findAll(Indicator indicator, Boolean active);

    ThresholdResponse findById(Long id);

    ThresholdResponse create(ThresholdRequest request);

    ThresholdResponse update(Long id, ThresholdRequest request);

    void delete(Long id);
}

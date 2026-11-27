package com.campusverse.ecocity.repository;

import com.campusverse.ecocity.entity.Threshold;
import com.campusverse.ecocity.enums.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {

    List<Threshold> findByIndicatorOrderById(Indicator indicator);

    List<Threshold> findByActiveOrderById(Boolean active);

    List<Threshold> findByIndicatorAndActiveOrderById(Indicator indicator, Boolean active);

    boolean existsByIndicatorAndActiveTrue(Indicator indicator);

    boolean existsByIndicatorAndActiveTrueAndIdNot(Indicator indicator, Long id);
}

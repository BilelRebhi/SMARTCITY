package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.EnvReportRequest;
import com.campusverse.ecocity.dto.EnvReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface EnvReportService {

    List<EnvReportResponse> findAll(Long zoneId, LocalDate from, LocalDate to);

    EnvReportResponse findById(Long id);

    EnvReportResponse create(EnvReportRequest request);

    EnvReportResponse update(Long id, EnvReportRequest request);

    void delete(Long id);
}

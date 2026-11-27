package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.ReadingRequest;
import com.campusverse.ecocity.dto.ReadingResponse;
import com.campusverse.ecocity.enums.Indicator;

import java.time.LocalDateTime;
import java.util.List;

/** CRUD for readings (the Measurement entity). */
public interface ReadingService {

    List<ReadingResponse> findAll(Long stationId, Indicator indicator, LocalDateTime from, LocalDateTime to);

    ReadingResponse findById(Long id);

    ReadingResponse create(ReadingRequest request);

    ReadingResponse update(Long id, ReadingRequest request);

    void delete(Long id);
}

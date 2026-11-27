package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.ReadingRequest;
import tn.esprit.ecocityservice.ecocity.dto.ReadingResponse;
import tn.esprit.ecocityservice.ecocity.enums.Indicator;

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

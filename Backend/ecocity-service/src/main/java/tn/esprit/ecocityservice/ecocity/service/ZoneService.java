package tn.esprit.ecocityservice.ecocity.service;

import tn.esprit.ecocityservice.ecocity.dto.ZoneConditionsResponse;
import tn.esprit.ecocityservice.ecocity.dto.ZoneRequest;
import tn.esprit.ecocityservice.ecocity.dto.ZoneResponse;

import java.util.List;

public interface ZoneService {

    List<ZoneResponse> findAll();

    ZoneResponse findById(Long id);

    ZoneResponse create(ZoneRequest request);

    ZoneResponse update(Long id, ZoneRequest request);

    void delete(Long id);

    /** Conditions in the citizen's zone. Asks MS1 for the citizen's neighborhood. */
    ZoneConditionsResponse getMyZoneConditions(Long citizenId);
}

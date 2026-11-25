package com.campusverse.ecocity.service;

import com.campusverse.ecocity.dto.ZoneRequest;
import com.campusverse.ecocity.dto.ZoneResponse;

import java.util.List;

public interface ZoneService {

    List<ZoneResponse> findAll();

    ZoneResponse findById(Long id);

    ZoneResponse create(ZoneRequest request);

    ZoneResponse update(Long id, ZoneRequest request);

    void delete(Long id);
}

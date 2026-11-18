package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.ResolutionConfirmation;

import java.util.List;
import java.util.Optional;

public interface ResolutionConfirmationService {
    
    ResolutionConfirmation createConfirmation(ResolutionConfirmation confirmation);
    
    Optional<ResolutionConfirmation> getConfirmationById(Long id);
    
    Optional<ResolutionConfirmation> getConfirmationByIncident(Long incidentId);
    
    List<ResolutionConfirmation> getAllConfirmations();
    
    void deleteConfirmation(Long id);
}

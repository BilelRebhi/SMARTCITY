package com.campusverse.municipal.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class MunicipalService {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private Integer processingDelayDays;

    private boolean requiresAppointment = false;

    // getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public Integer getProcessingDelayDays() { return processingDelayDays; }
    public void setProcessingDelayDays(Integer processingDelayDays) { this.processingDelayDays = processingDelayDays; }
    public boolean isRequiresAppointment() { return requiresAppointment; }
    public void setRequiresAppointment(boolean requiresAppointment) { this.requiresAppointment = requiresAppointment; }
}

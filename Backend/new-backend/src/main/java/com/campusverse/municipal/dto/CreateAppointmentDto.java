package com.campusverse.municipal.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CreateAppointmentDto {
    private UUID citizenId;
    private UUID applicationId;
    private LocalDate appointmentDate;
    private String timeSlot;
    private String location;

    public UUID getCitizenId() { return citizenId; }
    public void setCitizenId(UUID citizenId) { this.citizenId = citizenId; }
    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}

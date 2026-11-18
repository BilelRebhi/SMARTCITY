package com.smartcity.urbanincident.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resolution_confirmations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolutionConfirmation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "confirmation_date", nullable = false)
    private LocalDateTime confirmationDate;
    
    @Column(name = "incident_id", nullable = false)
    private Long incidentId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", insertable = false, updatable = false)
    private Incident incident;
    
    @PrePersist
    protected void onCreate() {
        confirmationDate = LocalDateTime.now();
    }
}

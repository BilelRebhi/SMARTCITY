package com.smartcity.urbanincident.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interventions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Intervention {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    
    @Column(name = "work_date")
    private LocalDate workDate;
    
    @Column(name = "work_description")
    private String workDescription;
    
    @Column(columnDefinition = "TEXT")
    private String result;
    
    @Column(name = "technician_id", nullable = false)
    private Long technicianId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;
}

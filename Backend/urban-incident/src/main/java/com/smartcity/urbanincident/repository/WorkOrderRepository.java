package com.smartcity.urbanincident.repository;

import com.smartcity.urbanincident.entity.WorkOrder;
import com.smartcity.urbanincident.enums.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    
    List<WorkOrder> findByIncidentId(Long incidentId);
    
    List<WorkOrder> findByTechnicianId(Long technicianId);
    
    List<WorkOrder> findByStatus(WorkOrderStatus status);
    
    List<WorkOrder> findByTechnicianIdAndStatus(Long technicianId, WorkOrderStatus status);
}

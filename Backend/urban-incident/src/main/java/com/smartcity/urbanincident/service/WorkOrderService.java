package com.smartcity.urbanincident.service;

import com.smartcity.urbanincident.entity.WorkOrder;
import com.smartcity.urbanincident.enums.WorkOrderStatus;

import java.util.List;
import java.util.Optional;

public interface WorkOrderService {
    
    WorkOrder createWorkOrder(WorkOrder workOrder);
    
    WorkOrder updateWorkOrder(Long id, WorkOrder workOrder);
    
    Optional<WorkOrder> getWorkOrderById(Long id);
    
    List<WorkOrder> getAllWorkOrders();
    
    List<WorkOrder> getWorkOrdersByIncident(Long incidentId);
    
    List<WorkOrder> getWorkOrdersByTechnician(Long technicianId);
    
    List<WorkOrder> getWorkOrdersByStatus(WorkOrderStatus status);
    
    WorkOrder assignTechnician(Long workOrderId, Long technicianId);
    
    WorkOrder updateWorkOrderStatus(Long id, WorkOrderStatus status);
    
    void deleteWorkOrder(Long id);
}

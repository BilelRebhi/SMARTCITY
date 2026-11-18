import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkOrder, WorkOrderStatus } from '../../shared/models/work-order.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WorkOrderService {
  private apiUrl = `${environment.apiUrl}/work-orders`;

  constructor(private http: HttpClient) {}

  createWorkOrder(workOrder: WorkOrder): Observable<WorkOrder> {
    // Implementation will be added
    return this.http.post<WorkOrder>(this.apiUrl, workOrder);
  }

  updateWorkOrder(id: number, workOrder: WorkOrder): Observable<WorkOrder> {
    // Implementation will be added
    return this.http.put<WorkOrder>(`${this.apiUrl}/${id}`, workOrder);
  }

  getWorkOrderById(id: number): Observable<WorkOrder> {
    // Implementation will be added
    return this.http.get<WorkOrder>(`${this.apiUrl}/${id}`);
  }

  getAllWorkOrders(): Observable<WorkOrder[]> {
    // Implementation will be added
    return this.http.get<WorkOrder[]>(this.apiUrl);
  }

  getWorkOrdersByIncident(incidentId: number): Observable<WorkOrder[]> {
    // Implementation will be added
    return this.http.get<WorkOrder[]>(`${this.apiUrl}/incident/${incidentId}`);
  }

  getWorkOrdersByTechnician(technicianId: number): Observable<WorkOrder[]> {
    // Implementation will be added
    return this.http.get<WorkOrder[]>(`${this.apiUrl}/technician/${technicianId}`);
  }

  getWorkOrdersByStatus(status: WorkOrderStatus): Observable<WorkOrder[]> {
    // Implementation will be added
    return this.http.get<WorkOrder[]>(`${this.apiUrl}/status/${status}`);
  }

  assignTechnician(workOrderId: number, technicianId: number): Observable<WorkOrder> {
    // Implementation will be added
    return this.http.patch<WorkOrder>(`${this.apiUrl}/${workOrderId}/assign`, null, {
      params: { technicianId: technicianId.toString() }
    });
  }

  updateWorkOrderStatus(id: number, status: WorkOrderStatus): Observable<WorkOrder> {
    // Implementation will be added
    return this.http.patch<WorkOrder>(`${this.apiUrl}/${id}/status`, null, {
      params: { status }
    });
  }

  deleteWorkOrder(id: number): Observable<void> {
    // Implementation will be added
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

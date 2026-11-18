import { Priority } from './incident.model';

export interface WorkOrder {
  id?: number;
  title: string;
  description: string;
  priority?: Priority;
  status: WorkOrderStatus;
  plannedDate?: Date;
  startTime?: string;
  estimatedDuration?: number;
  technicianId?: number;
  incidentId: number;
}

export enum WorkOrderStatus {
  PENDING = 'PENDING',
  ASSIGNED = 'ASSIGNED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

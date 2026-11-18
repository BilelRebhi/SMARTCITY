export interface Intervention {
  id?: number;
  startDateTime?: Date;
  workDate?: Date;
  workDescription: string;
  result: string;
  technicianId: number;
  workOrderId: number;
}

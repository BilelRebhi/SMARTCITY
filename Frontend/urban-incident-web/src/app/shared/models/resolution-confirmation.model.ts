export interface ResolutionConfirmation {
  id?: number;
  isResolved: boolean;
  feedback: string;
  confirmationDate?: Date;
  incidentId: number;
}

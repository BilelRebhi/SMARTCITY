export type RequestStatus =
  | 'PENDING'
  | 'IN_REVIEW'
  | 'MISSING_DOCUMENTS'
  | 'APPROVED'
  | 'REJECTED'
  | 'COMPLETED';

export type AppointmentStatus = 'SCHEDULED' | 'COMPLETED' | 'CANCELLED';

export interface MunicipalService {
  id: string;
  name: string;
  category?: string;
  description: string;
  requirements?: string;
  processingDelayDays: number;
  requiresAppointment: boolean;
}

export interface ApplicationRequest {
  id: string;
  citizenId: string;
  citizenName?: string;
  citizenEmail?: string;
  notes?: string;
  service: MunicipalService;
  status: RequestStatus;
  validatedComplete: boolean;
  adminComment?: string;
  submittedAt?: string;
  updatedAt?: string;
  completedAt?: string;
}

export interface CreateApplicationDto {
  citizenId?: string;
  citizenName?: string;
  citizenEmail?: string;
  notes?: string;
  serviceId: string;
}

export interface UpdateStatusDto {
  status: RequestStatus;
  comment?: string;
}

export interface ValidateApplicationDto {
  complete: boolean;
  comment?: string;
}

export interface SupportingDocument {
  id: string;
  fileName: string;
  contentType: string;
  fileSize: number;
  uploadedAt: string;
}

export interface Appointment {
  id: string;
  citizenId: string;
  application: ApplicationRequest;
  appointmentDate: string;
  timeSlot: string;
  location: string;
  status: AppointmentStatus;
  createdAt: string;
}

export interface CreateAppointmentDto {
  citizenId?: string;
  applicationId: string;
  appointmentDate: string;
  timeSlot: string;
  location?: string;
}

export interface MunicipalNotification {
  id: string;
  citizenId: string;
  applicationId?: string;
  title: string;
  message: string;
  type: 'INFO' | 'WARNING' | 'SUCCESS' | 'ERROR';
  read: boolean;
  createdAt: string;
}

export interface MunicipalStatistics {
  totalRequests: number;
  pendingCount: number;
  inReviewCount: number;
  missingDocumentsCount: number;
  approvedCount: number;
  rejectedCount: number;
  completedCount: number;
  approvalRate: number;
  averageProcessingTimeHours: number;
  requestsByService: Record<string, number>;
  requestsByStatus: Record<string, number>;
}

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import {
  ApplicationRequest,
  Appointment,
  CreateApplicationDto,
  CreateAppointmentDto,
  MunicipalNotification,
  MunicipalService,
  MunicipalStatistics,
  RequestStatus,
  SupportingDocument,
  UpdateStatusDto,
  ValidateApplicationDto
} from '../models/municipal.model';

@Injectable({ providedIn: 'root' })
export class MunicipalServiceApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/municipal';
  private readonly citizenStorageKey = 'campusverse_municipal_citizen_id';

  getOrCreateCitizenId(): string {
    let id = localStorage.getItem(this.citizenStorageKey);
    if (!id) {
      id = crypto.randomUUID();
      localStorage.setItem(this.citizenStorageKey, id);
    }
    return id;
  }

  setActiveCitizenId(id: string): void {
    if (id.trim()) {
      localStorage.setItem(this.citizenStorageKey, id.trim());
    }
  }

  // ---------------------- 1. Services Catalogue ----------------------

  getServices(): Observable<MunicipalService[]> {
    return this.http.get<MunicipalService[]>(`${this.baseUrl}/services`).pipe(
      catchError(this.handleError)
    );
  }

  getServiceById(id: string): Observable<MunicipalService> {
    return this.http.get<MunicipalService>(`${this.baseUrl}/services/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createService(service: Partial<MunicipalService>): Observable<MunicipalService> {
    return this.http.post<MunicipalService>(`${this.baseUrl}/services`, service).pipe(
      catchError(this.handleError)
    );
  }

  // ---------------------- 2, 6, 9. Applications & History ----------------------

  getAllApplications(): Observable<ApplicationRequest[]> {
    return this.http.get<ApplicationRequest[]>(`${this.baseUrl}/applications`).pipe(
      catchError(this.handleError)
    );
  }

  submitApplication(dto: CreateApplicationDto): Observable<ApplicationRequest> {
    return this.http.post<ApplicationRequest>(`${this.baseUrl}/applications`, dto).pipe(
      catchError(this.handleError)
    );
  }

  getApplicationById(id: string): Observable<ApplicationRequest> {
    return this.http.get<ApplicationRequest>(`${this.baseUrl}/applications/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getApplicationsByCitizen(citizenId: string): Observable<ApplicationRequest[]> {
    return this.http.get<ApplicationRequest[]>(`${this.baseUrl}/applications/citizen/${citizenId}`).pipe(
      catchError(this.handleError)
    );
  }

  // ---------------------- 3. Supporting Documents ----------------------

  uploadDocument(applicationId: string, file: File): Observable<SupportingDocument> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<SupportingDocument>(`${this.baseUrl}/applications/${applicationId}/documents`, formData).pipe(
      catchError(this.handleError)
    );
  }

  getDocuments(applicationId: string): Observable<SupportingDocument[]> {
    return this.http.get<SupportingDocument[]>(`${this.baseUrl}/applications/${applicationId}/documents`).pipe(
      catchError(this.handleError)
    );
  }

  getDocumentDownloadUrl(docId: string): string {
    return `${this.baseUrl}/documents/${docId}/download`;
  }

  // ---------------------- 4 & 5. Validation & Processing ----------------------

  validateApplication(id: string, complete: boolean, comment?: string): Observable<ApplicationRequest> {
    const payload: ValidateApplicationDto = { complete, comment };
    return this.http.patch<ApplicationRequest>(`${this.baseUrl}/applications/${id}/validate`, payload).pipe(
      catchError(this.handleError)
    );
  }

  updateApplicationStatus(id: string, status: RequestStatus, comment?: string): Observable<ApplicationRequest> {
    const payload: UpdateStatusDto = { status, comment };
    return this.http.patch<ApplicationRequest>(`${this.baseUrl}/applications/${id}/status`, payload).pipe(
      catchError(this.handleError)
    );
  }

  // ---------------------- 7. Appointments ----------------------

  scheduleAppointment(dto: CreateAppointmentDto): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.baseUrl}/appointments`, dto).pipe(
      catchError(this.handleError)
    );
  }

  getAppointmentsByCitizen(citizenId: string): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/appointments/citizen/${citizenId}`).pipe(
      catchError(this.handleError)
    );
  }

  getAppointmentsByApplication(applicationId: string): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/appointments/application/${applicationId}`).pipe(
      catchError(this.handleError)
    );
  }

  cancelAppointment(id: string): Observable<Appointment> {
    return this.http.patch<Appointment>(`${this.baseUrl}/appointments/${id}/cancel`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // ---------------------- 8. Notifications ----------------------

  getNotificationsByCitizen(citizenId: string): Observable<MunicipalNotification[]> {
    return this.http.get<MunicipalNotification[]>(`${this.baseUrl}/notifications/citizen/${citizenId}`).pipe(
      catchError(this.handleError)
    );
  }

  markNotificationAsRead(id: string): Observable<MunicipalNotification> {
    return this.http.patch<MunicipalNotification>(`${this.baseUrl}/notifications/${id}/read`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // ---------------------- 10. Statistics ----------------------

  getStatistics(): Observable<MunicipalStatistics> {
    return this.http.get<MunicipalStatistics>(`${this.baseUrl}/statistics`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(err: HttpErrorResponse): Observable<never> {
    let msg = 'An unexpected error occurred';
    if (err.status === 0) {
      msg = 'Cannot reach Municipal Service backend. Is it running (8085) or via Gateway (8087)?';
    } else if (err.error?.message) {
      msg = err.error.message;
    } else if (err.statusText) {
      msg = `${err.status}: ${err.statusText}`;
    }
    return throwError(() => new Error(msg));
  }
}

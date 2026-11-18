import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Intervention } from '../../shared/models/intervention.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InterventionService {
  private apiUrl = `${environment.apiUrl}/interventions`;

  constructor(private http: HttpClient) {}

  createIntervention(intervention: Intervention): Observable<Intervention> {
    // Implementation will be added
    return this.http.post<Intervention>(this.apiUrl, intervention);
  }

  updateIntervention(id: number, intervention: Intervention): Observable<Intervention> {
    // Implementation will be added
    return this.http.put<Intervention>(`${this.apiUrl}/${id}`, intervention);
  }

  getInterventionById(id: number): Observable<Intervention> {
    // Implementation will be added
    return this.http.get<Intervention>(`${this.apiUrl}/${id}`);
  }

  getAllInterventions(): Observable<Intervention[]> {
    // Implementation will be added
    return this.http.get<Intervention[]>(this.apiUrl);
  }

  getInterventionByWorkOrder(workOrderId: number): Observable<Intervention> {
    // Implementation will be added
    return this.http.get<Intervention>(`${this.apiUrl}/work-order/${workOrderId}`);
  }

  getInterventionsByTechnician(technicianId: number): Observable<Intervention[]> {
    // Implementation will be added
    return this.http.get<Intervention[]>(`${this.apiUrl}/technician/${technicianId}`);
  }

  deleteIntervention(id: number): Observable<void> {
    // Implementation will be added
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

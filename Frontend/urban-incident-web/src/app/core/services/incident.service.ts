import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Incident, IncidentStatus } from '../../shared/models/incident.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {
  private apiUrl = `${environment.apiUrl}/incidents`;

  constructor(private http: HttpClient) {}

  createIncident(incident: Incident): Observable<Incident> {
    // Implementation will be added
    return this.http.post<Incident>(this.apiUrl, incident);
  }

  updateIncident(id: number, incident: Incident): Observable<Incident> {
    // Implementation will be added
    return this.http.put<Incident>(`${this.apiUrl}/${id}`, incident);
  }

  getIncidentById(id: number): Observable<Incident> {
    // Implementation will be added
    return this.http.get<Incident>(`${this.apiUrl}/${id}`);
  }

  getAllIncidents(): Observable<Incident[]> {
    // Implementation will be added
    return this.http.get<Incident[]>(this.apiUrl);
  }

  getIncidentsByCitizen(citizenId: number): Observable<Incident[]> {
    // Implementation will be added
    return this.http.get<Incident[]>(`${this.apiUrl}/citizen/${citizenId}`);
  }

  getIncidentsByStatus(status: IncidentStatus): Observable<Incident[]> {
    // Implementation will be added
    return this.http.get<Incident[]>(`${this.apiUrl}/status/${status}`);
  }

  updateIncidentStatus(id: number, status: IncidentStatus): Observable<Incident> {
    // Implementation will be added
    return this.http.patch<Incident>(`${this.apiUrl}/${id}/status`, null, {
      params: { status }
    });
  }

  deleteIncident(id: number): Observable<void> {
    // Implementation will be added
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

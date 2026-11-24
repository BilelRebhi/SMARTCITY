import { DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApplicationRequest, Appointment, SupportingDocument } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-tracking',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './tracking.html',
  styleUrl: './tracking.css'
})
export class TrackingComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);
  private readonly route = inject(ActivatedRoute);

  searchId = '';
  loading = false;
  errorMessage: string | null = null;
  application: ApplicationRequest | null = null;
  documents: SupportingDocument[] = [];
  appointments: Appointment[] = [];

  ngOnInit(): void {
    const paramId = this.route.snapshot.paramMap.get('id');
    if (paramId) {
      this.searchId = paramId;
      this.fetchApplication(paramId);
    }
  }

  onSearch(): void {
    if (!this.searchId.trim()) return;
    this.fetchApplication(this.searchId.trim());
  }

  fetchApplication(id: string): void {
    this.loading = true;
    this.errorMessage = null;

    this.api.getApplicationById(id).subscribe({
      next: (app) => {
        this.loading = false;
        this.application = app;
        this.loadDocuments(app.id);
        this.loadAppointments(app.id);
      },
      error: (err) => {
        this.loading = false;
        this.application = null;
        this.errorMessage = err.message || 'Application not found with that ID';
      }
    });
  }

  loadDocuments(appId: string): void {
    this.api.getDocuments(appId).subscribe({
      next: (docs) => (this.documents = docs),
      error: () => (this.documents = [])
    });
  }

  loadAppointments(appId: string): void {
    this.api.getAppointmentsByApplication(appId).subscribe({
      next: (apts) => (this.appointments = apts),
      error: () => (this.appointments = [])
    });
  }

  onUploadDoc(event: Event): void {
    if (!this.application) return;
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.api.uploadDocument(this.application.id, file).subscribe({
      next: () => {
        input.value = '';
        if (this.application) {
          this.fetchApplication(this.application.id);
        }
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }

  getDownloadUrl(docId: string): string {
    return this.api.getDocumentDownloadUrl(docId);
  }

  isStepCompleted(step: number): boolean {
    if (!this.application) return false;
    const s = this.application.status;
    if (step === 1) return s !== 'PENDING';
    if (step === 2) return s === 'APPROVED' || s === 'REJECTED' || s === 'COMPLETED';
    if (step === 3) return s === 'APPROVED' || s === 'COMPLETED';
    return false;
  }

  isStepActive(step: number): boolean {
    if (!this.application) return false;
    const s = this.application.status;
    if (step === 1) return s === 'PENDING';
    if (step === 2) return s === 'IN_REVIEW' || s === 'MISSING_DOCUMENTS';
    if (step === 3) return s === 'APPROVED' || s === 'REJECTED' || s === 'COMPLETED';
    return false;
  }
}

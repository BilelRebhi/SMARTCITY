import { DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ApplicationRequest,
  RequestStatus,
  SupportingDocument
} from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-employee-desk',
  imports: [FormsModule, DatePipe],
  templateUrl: './employee-desk.html',
  styleUrl: './employee-desk.css'
})
export class EmployeeDeskComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);

  applications: ApplicationRequest[] = [];
  selectedApp: ApplicationRequest | null = null;
  selectedDocs: SupportingDocument[] = [];
  statusFilter = '';
  commentInput = '';
  loading = true;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.loading = true;
    this.api.getAllApplications().subscribe({
      next: (data) => {
        this.applications = data;
        this.loading = false;
        if (this.selectedApp) {
          const refreshed = data.find(a => a.id === this.selectedApp!.id);
          if (refreshed) this.selectedApp = refreshed;
        }
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      }
    });
  }

  get filteredApplications(): ApplicationRequest[] {
    if (!this.statusFilter) return this.applications;
    return this.applications.filter(a => a.status === this.statusFilter);
  }

  selectApplication(app: ApplicationRequest): void {
    this.selectedApp = app;
    this.commentInput = app.adminComment || '';
    this.successMessage = null;
    this.errorMessage = null;
    this.api.getDocuments(app.id).subscribe({
      next: (docs) => (this.selectedDocs = docs),
      error: () => (this.selectedDocs = [])
    });
  }

  getDownloadUrl(docId: string): string {
    return this.api.getDocumentDownloadUrl(docId);
  }

  validate(complete: boolean): void {
    if (!this.selectedApp) return;
    this.errorMessage = null;
    this.successMessage = null;

    this.api.validateApplication(this.selectedApp.id, complete, this.commentInput).subscribe({
      next: (updated) => {
        this.selectedApp = updated;
        this.successMessage = complete
          ? 'Application marked complete and moved to IN_REVIEW.'
          : 'Citizen notified of missing documents.';
        this.loadApplications();
      },
      error: (err) => (this.errorMessage = err.message)
    });
  }

  processStatus(status: RequestStatus): void {
    if (!this.selectedApp) return;
    this.errorMessage = null;
    this.successMessage = null;

    this.api.updateApplicationStatus(this.selectedApp.id, status, this.commentInput).subscribe({
      next: (updated) => {
        this.selectedApp = updated;
        this.successMessage = `Application updated to ${status}. Citizen notification sent.`;
        this.loadApplications();
      },
      error: (err) => (this.errorMessage = err.message)
    });
  }
}

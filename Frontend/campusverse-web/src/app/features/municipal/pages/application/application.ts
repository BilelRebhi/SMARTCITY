import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { ApplicationRequest, MunicipalService } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-application',
  imports: [FormsModule, RouterLink],
  templateUrl: './application.html',
  styleUrl: './application.css'
})
export class ApplicationComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);
  private readonly route = inject(ActivatedRoute);

  services: MunicipalService[] = [];
  selectedServiceId = '';
  selectedService: MunicipalService | null = null;
  citizenId = '';
  citizenName = '';
  citizenEmail = '';
  notes = '';
  selectedFiles: File[] = [];

  submitting = false;
  errorMessage: string | null = null;
  submittedApplication: ApplicationRequest | null = null;
  uploadedCount = 0;

  ngOnInit(): void {
    const paramServiceId = this.route.snapshot.paramMap.get('serviceId');

    this.api.getServices().subscribe({
      next: (services) => {
        this.services = services;
        if (paramServiceId && this.services.some(s => s.id === paramServiceId)) {
          this.selectedServiceId = paramServiceId;
          this.onServiceChange();
        }
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });

    this.citizenId = this.api.getOrCreateCitizenId();
  }

  onServiceChange(): void {
    this.selectedService = this.services.find(s => s.id === this.selectedServiceId) || null;
  }

  generateCitizenId(): void {
    this.citizenId = crypto.randomUUID();
    this.api.setActiveCitizenId(this.citizenId);
  }

  onFilesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      for (let i = 0; i < input.files.length; i++) {
        this.selectedFiles.push(input.files[i]);
      }
      input.value = '';
    }
  }

  removeFile(index: number): void {
    this.selectedFiles.splice(index, 1);
  }

  formatSize(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  }

  parseRequirements(req: string): string[] {
    return req.split(';').map(r => r.trim()).filter(r => r.length > 0);
  }

  onSubmit(): void {
    if (!this.selectedServiceId) {
      this.errorMessage = 'Please select a service';
      return;
    }

    if (this.citizenId) {
      this.api.setActiveCitizenId(this.citizenId);
    }

    this.submitting = true;
    this.errorMessage = null;

    this.api.submitApplication({
      serviceId: this.selectedServiceId,
      citizenId: this.citizenId || undefined,
      citizenName: this.citizenName || undefined,
      citizenEmail: this.citizenEmail || undefined,
      notes: this.notes || undefined
    }).subscribe({
      next: (app) => {
        if (this.selectedFiles.length === 0) {
          this.submitting = false;
          this.uploadedCount = 0;
          this.submittedApplication = app;
          return;
        }

        const uploads = this.selectedFiles.map(file => this.api.uploadDocument(app.id, file));
        forkJoin(uploads.length ? uploads : [of(null)]).subscribe({
          next: (docs) => {
            this.submitting = false;
            this.uploadedCount = docs.length;
            this.submittedApplication = app;
          },
          error: (err) => {
            this.submitting = false;
            this.uploadedCount = 0;
            this.submittedApplication = app;
            this.errorMessage = `Application saved, but file upload failed: ${err.message}`;
          }
        });
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.message;
      }
    });
  }

  resetForm(): void {
    this.submittedApplication = null;
    this.selectedServiceId = '';
    this.selectedService = null;
    this.notes = '';
    this.selectedFiles = [];
    this.uploadedCount = 0;
  }
}

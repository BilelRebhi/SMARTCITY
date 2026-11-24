import { DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import {
  ApplicationRequest,
  Appointment,
  MunicipalNotification
} from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-citizen-portal',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './citizen-portal.html',
  styleUrl: './citizen-portal.css'
})
export class CitizenPortalComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);
  private readonly route = inject(ActivatedRoute);

  citizenId = '';
  applications: ApplicationRequest[] = [];
  appointments: Appointment[] = [];
  notifications: MunicipalNotification[] = [];
  errorMessage: string | null = null;
  successMessage: string | null = null;

  appointmentForm = {
    applicationId: '',
    appointmentDate: '',
    timeSlot: '09:00 - 09:30',
    location: 'City Hall - Municipal Counter B'
  };

  ngOnInit(): void {
    this.citizenId = this.api.getOrCreateCitizenId();
    const bookApp = this.route.snapshot.queryParamMap.get('bookApp');
    if (bookApp) {
      this.appointmentForm.applicationId = bookApp;
    }
    this.loadAll();
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  loadAll(): void {
    if (!this.citizenId.trim()) return;
    this.api.setActiveCitizenId(this.citizenId);
    this.errorMessage = null;

    this.api.getApplicationsByCitizen(this.citizenId).subscribe({
      next: (apps) => (this.applications = apps),
      error: (err) => (this.errorMessage = err.message)
    });

    this.loadAppointments();
    this.loadNotifications();
  }

  loadAppointments(): void {
    this.api.getAppointmentsByCitizen(this.citizenId).subscribe({
      next: (apts) => (this.appointments = apts),
      error: () => (this.appointments = [])
    });
  }

  loadNotifications(): void {
    this.api.getNotificationsByCitizen(this.citizenId).subscribe({
      next: (notifs) => (this.notifications = notifs),
      error: () => (this.notifications = [])
    });
  }

  markRead(id: string): void {
    this.api.markNotificationAsRead(id).subscribe({
      next: (updated) => {
        const target = this.notifications.find(n => n.id === id);
        if (target) target.read = updated.read;
      }
    });
  }

  prefillAppointment(applicationId: string): void {
    this.appointmentForm.applicationId = applicationId;
  }

  bookAppointment(): void {
    if (!this.appointmentForm.applicationId || !this.appointmentForm.appointmentDate) return;
    this.errorMessage = null;
    this.successMessage = null;

    this.api.scheduleAppointment({
      citizenId: this.citizenId,
      applicationId: this.appointmentForm.applicationId,
      appointmentDate: this.appointmentForm.appointmentDate,
      timeSlot: this.appointmentForm.timeSlot,
      location: this.appointmentForm.location
    }).subscribe({
      next: () => {
        this.successMessage = 'Appointment scheduled successfully!';
        this.loadAppointments();
        this.loadNotifications();
      },
      error: (err) => (this.errorMessage = err.message)
    });
  }

  cancelAppointment(id: string): void {
    this.api.cancelAppointment(id).subscribe({
      next: () => {
        this.loadAppointments();
        this.loadNotifications();
      },
      error: (err) => (this.errorMessage = err.message)
    });
  }
}

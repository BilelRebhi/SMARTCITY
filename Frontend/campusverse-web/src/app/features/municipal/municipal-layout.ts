import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-municipal-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="municipal-shell">
      <nav class="subnav card">
        <a routerLink="/municipal" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">
          Service Catalogue
        </a>
        <a routerLink="/municipal/apply" routerLinkActive="active">
          Apply Online
        </a>
        <a routerLink="/municipal/track" routerLinkActive="active">
          Track Request
        </a>
        <a routerLink="/municipal/portal" routerLinkActive="active">
          My History, Appointments &amp; Notifications
        </a>
        <a routerLink="/municipal/employee" routerLinkActive="active">
          Employee Validation Desk
        </a>
        <a routerLink="/municipal/statistics" routerLinkActive="active">
          Statistics
        </a>
      </nav>
      <router-outlet />
    </div>
  `,
  styles: `
    .subnav {
      display: flex;
      gap: 0.5rem;
      flex-wrap: wrap;
      padding: 0.65rem 1rem;
      margin-bottom: 1.5rem;
    }
    .subnav a {
      text-decoration: none;
      color: var(--text);
      padding: 0.4rem 0.85rem;
      border-radius: 6px;
      font-size: 0.9rem;
      font-weight: 500;
      transition: background 0.15s, color 0.15s;
    }
    .subnav a:hover {
      background: var(--bg);
    }
    .subnav a.active {
      background: var(--primary);
      color: #fff;
    }
  `
})
export class MunicipalLayoutComponent {}

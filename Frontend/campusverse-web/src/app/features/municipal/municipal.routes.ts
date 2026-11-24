import { Routes } from '@angular/router';
import { MunicipalLayoutComponent } from './municipal-layout';
import { ApplicationComponent } from './pages/application/application';
import { CatalogueComponent } from './pages/catalogue/catalogue';
import { CitizenPortalComponent } from './pages/citizen-portal/citizen-portal';
import { EmployeeDeskComponent } from './pages/employee-desk/employee-desk';
import { StatisticsComponent } from './pages/statistics/statistics';
import { TrackingComponent } from './pages/tracking/tracking';

export const MUNICIPAL_ROUTES: Routes = [
  {
    path: '',
    component: MunicipalLayoutComponent,
    children: [
      { path: '', component: CatalogueComponent, title: 'Municipal Services | CampusVerse' },
      { path: 'apply', component: ApplicationComponent, title: 'Apply for Municipal Service | CampusVerse' },
      { path: 'apply/:serviceId', component: ApplicationComponent, title: 'Apply for Municipal Service | CampusVerse' },
      { path: 'track', component: TrackingComponent, title: 'Track Municipal Request | CampusVerse' },
      { path: 'track/:id', component: TrackingComponent, title: 'Track Municipal Request | CampusVerse' },
      { path: 'portal', component: CitizenPortalComponent, title: 'Citizen Portal | CampusVerse' },
      { path: 'employee', component: EmployeeDeskComponent, title: 'Employee Validation Desk | CampusVerse' },
      { path: 'statistics', component: StatisticsComponent, title: 'Municipal Statistics | CampusVerse' }
    ]
  }
];

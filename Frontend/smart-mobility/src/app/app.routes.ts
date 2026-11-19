import { Routes } from '@angular/router';
import { RouteList } from './pages/route-list/route-list';
import { RouteForm } from './pages/route-form/route-form';
import { RouteDetail } from './pages/route-detail/route-detail';
import { RoadSegments } from './pages/road-segments/road-segments';
import { TrafficConditions } from './pages/traffic-conditions/traffic-conditions';
import { AccessiblePlaces } from './pages/accessible-places/accessible-places';
import { AccessibilityFeedbackPage } from './pages/accessibility-feedback/accessibility-feedback';

export const routes: Routes = [
  { path: '', component: RouteList, title: 'Routes | Smart Mobility' },
  { path: 'new', component: RouteForm, title: 'New Route | Smart Mobility' },
  { path: 'road-segments', component: RoadSegments, title: 'Road Segments | Smart Mobility' },
  { path: 'traffic', component: TrafficConditions, title: 'Traffic | Smart Mobility' },
  { path: 'accessible-places', component: AccessiblePlaces, title: 'Accessible Places | Smart Mobility' },
  { path: 'feedback', component: AccessibilityFeedbackPage, title: 'Feedback | Smart Mobility' },
  { path: ':id', component: RouteDetail, title: 'Route Detail | Smart Mobility' },
  { path: '**', redirectTo: '' },
];

import { Routes } from '@angular/router';
import { Home } from './pages/home/home';

/**
 * Each microservice owner adds ONE line here pointing to their feature folder:
 *   { path: 'spaces', loadChildren: () => import('./features/spaces/spaces.routes').then(m => m.SPACES_ROUTES) },
 */
export const routes: Routes = [
  { path: '', component: Home, title: 'CampusVerse' },

  // Municipal Services (Bilel)
  { path: 'municipal', loadChildren: () => import('./features/municipal/municipal.routes').then(m => m.MUNICIPAL_ROUTES) },

  { path: '**', redirectTo: '' }
];

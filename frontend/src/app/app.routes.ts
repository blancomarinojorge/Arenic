import { Routes } from '@angular/router';
import { AuthGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./core/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'clubs',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/club-search/club-search.component').then(m => m.ClubSearchComponent)
  },
  {
    path: 'clubs/:id',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/club-detail/club-detail.component').then(m => m.ClubDetailComponent)
  },
  {
    path: 'dashboard',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  { path: '', redirectTo: 'clubs', pathMatch: 'full' },
  { path: '**', redirectTo: 'clubs' }
];

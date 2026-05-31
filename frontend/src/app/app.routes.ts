import { Routes } from '@angular/router';
import { AuthGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  /*todo implement the routes login and dashboard*/
  {
    path: 'login',
    loadComponent: () =>
      import('./core/auth/login/login.component')
        .then(m => m.LoginComponent)
  },
  {
    path: 'dashboard',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component')
        .then(m => m.DashboardComponent)
  },
  {path: '', redirectTo: 'dashboard', pathMatch: "full"},
  {path: '**', redirectTo: 'dashboard'}
];

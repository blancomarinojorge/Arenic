import { Routes } from '@angular/router';
import {AuthCallbackComponent} from './auth/components/auth-callback/auth-callback.component';
import {DashboardComponent} from './dashboard/dashboard.component';

export const routes: Routes = [
  {path: 'auth-callback', component: AuthCallbackComponent},
  {path: 'dashboard', component: DashboardComponent},
];

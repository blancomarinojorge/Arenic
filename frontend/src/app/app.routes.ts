import { Routes } from '@angular/router';
import {AuthCallbackComponent} from './auth/components/auth-callback/auth-callback.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {LoginComponent} from './auth/login/login-component/login-component.component';

export const routes: Routes = [
  {path: 'auth-callback', component: AuthCallbackComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'login', component: LoginComponent},
];

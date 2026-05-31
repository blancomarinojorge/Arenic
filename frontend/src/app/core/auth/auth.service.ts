import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { enviroment } from '../../../enviroments/enviroment';
import {AuthResponse, GoogleLoginRequest, LoginRequest} from './auth.models';


const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

@Injectable({providedIn : 'root'})
export class AuthService{
  /* Creates a signal that holds either a string or null. And the initial value is whatever
  *   is currently in localStorage
  * */
  private readonly _accessToken = signal<string|null>(
    localStorage.getItem(ACCESS_TOKEN_KEY)
  )
  readonly isAuthenticated = computed(() => !!this._accessToken())

  constructor(private http: HttpClient, private router: Router) {}

  getAccessToken(): string | null{
    return this._accessToken();
  }

  login(request: LoginRequest){
    return this.http
      .post<AuthResponse>(`${enviroment.apiUrl}/auth/login`, request)
      .pipe(tap(response => this.storeTokens(response)))
  }

  googleLogin(request: GoogleLoginRequest){
    return this.http
      .post<AuthResponse>(`${enviroment.apiUrl}/auth/googleLogin`, request)
      .pipe(tap(response => this.storeTokens(response)));
  }

  logout(){
    const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY);
    if (refreshToken){
      this.http
        .post(`${enviroment.apiUrl}/auth/logout`, {refreshToken})
        .subscribe({complete: () => this.clearTokens()})
    }else {
      this.clearTokens();
    }
  }

  refreshAccessToken(){
    const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY);
    if (!refreshToken) return null;

    return this.http
      .post<AuthResponse>(`${enviroment.apiUrl}/auth/refresh`, refreshToken)
      .pipe(tap(response => this.storeTokens(response)));
  }

  private storeTokens(response: AuthResponse){
    localStorage.setItem(ACCESS_TOKEN_KEY, response.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, response.refreshToken);
    this._accessToken.set(response.accessToken);
  }

  private clearTokens(){
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    this._accessToken.set(null);
    this.router.navigate(['/login'])
  }
}

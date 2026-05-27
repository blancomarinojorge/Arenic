import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject = new BehaviorSubject<any>(null);
  public user$ = this.userSubject.asObservable();

  constructor(private http:HttpClient) { }

  fetchUser(): Observable<object>{
    return this.http.get("http://localhost:8080/api/secured",{
      withCredentials: true
    });
  }

  loginWithGoogle(){
    window.location.href = 'http://localhost:8080/oauth2/authorization/google'
  }

  setUser(userData: any){
    this.userSubject.next(userData);
  }
}

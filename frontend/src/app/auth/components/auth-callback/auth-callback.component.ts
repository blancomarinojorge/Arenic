import { Component } from '@angular/core';
import {AuthService} from '../../auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-auth-callback',
  standalone: true,
  imports: [],
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.css'
})
export class AuthCallbackComponent {
  constructor(private authService: AuthService, private router:Router) {}

  ngOnInit(): void{
    this.authService.fetchUser().subscribe({
      next: data => {
        this.authService.setUser(data);
        this.router.navigate(['/']);
      },
      error: err => {
        console.error('No valid session found', err);
        this.router.navigate(['/']);
      }
    })
  }
}

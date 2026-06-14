import {Component, inject} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {AuthService} from '../../../../core/auth/auth.service';
import {AppIconComponent} from '../../icons/app-icon/app-icon.component';

@Component({
  selector: 'app-public-app-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    AppIconComponent
  ],
  templateUrl: './public-app-layout.component.html',
  styleUrl: './public-app-layout.component.css'
})
export class PublicAppLayoutComponent {
  authservice: AuthService = inject(AuthService);
}

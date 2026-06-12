import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import {Component, signal, inject, NgZone} from '@angular/core';
import {enviroment} from '../../../../enviroments/enviroment';
import {GoogleLoginRequest} from '../auth.models';
import {
  TextInputComponent
} from '../../../shared/components/forms/text-input-component/text-input.component';
import {AppIconComponent} from '../../../shared/components/icons/app-icon/app-icon.component';

/*since we add the google api via the script sdk, typescript doesnt know it exists
We have to create the variable to be able to access it*/
declare const google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, TextInputComponent, AppIconComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private fb: FormBuilder = inject(FormBuilder);
  private authService: AuthService = inject(AuthService);
  private router: Router = inject(Router);
  //used for
  private ngZone: NgZone = inject(NgZone);

  loading = signal(false);
  errorMessage = signal('');


  /*****************
  * User and password form validation
  * */
  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });
  get email() { return this.form.controls.email }
  get password() { return this.form.controls.password }

  onSubmit() {
    if (this.form.invalid || this.loading()) return;

    this.loading.set(true);
    this.errorMessage.set('');

    this.authService.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/clubs']),
      error: (err) => {
        this.errorMessage.set(
          err.status === 401
            ? 'Invalid email or password'
            : 'Something went wrong. Try again.'
        )
        this.loading.set(false);
      }
    })
  }


  /*****************
   * Google validation
   * */

  ngOnInit() {
    console.log(enviroment.googleClientId);
    google.accounts.id.initialize({
      client_id: enviroment.googleClientId,
      callback: (response: any) => {
        /*
        * We call ngZone.run because google callback runs outside angular
        * challenge detection zone, meaning it wouldnt notice signal state
        * change. For example, this.loading.set()...
        *
        * To prevent that, we execute the callback inside angular
        * scope using ngZone.
        * */
        this.ngZone.run(() => this.handleGoogleResponse(response));
      }
    });
  }

  handleGoogleResponse(response: any){
    this.loading.set(true);
    this.errorMessage.set('');

    /*Get the token returned by google so we can log in in our backend*/
    const googleLoginRequest: GoogleLoginRequest = {idToken: response.credential};

    this.authService.googleLogin(googleLoginRequest).subscribe({
      next: () => this.router.navigate(['/clubs']),
      error: () => {
        this.errorMessage.set('Google sign-in failed. Try again.');
        this.loading.set(false);
      }
    })
  }

  onGoogleButtonClick(){
    google.accounts.id.prompt();
  }
}

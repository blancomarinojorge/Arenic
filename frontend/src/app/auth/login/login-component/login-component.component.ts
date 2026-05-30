import { Component, OnInit, NgZone } from '@angular/core';

// Declare google as a global variable so TypeScript doesn't throw errors
declare var google: any;

@Component({
  selector: 'app-login',
  templateUrl: './login-component.component.html',
  standalone: true,
  styleUrls: ['./login-component.component.css']
})
export class LoginComponent implements OnInit {

  // Inject NgZone to handle asynchronous backend responses inside Angular's change detection
  constructor(private ngZone: NgZone) {}

  ngOnInit(): void {
    // Initialize Google Sign-In
    google.accounts.id.initialize({
      client_id: "442745530537-b3iv2o4v4rs28rfik74skgvarcc0dgnv.apps.googleusercontent.com",
      callback: (response: any) => this.handleCredentialResponse(response)
    });

    // Render the button
    google.accounts.id.renderButton(
      document.getElementById("buttonDiv"),
      { theme: "outline", size: "large" }
    );

    // Optional One Tap prompt
    google.accounts.id.prompt();
  }

  handleCredentialResponse(response: any) {
    const idToken = response.credential;
    console.log("Obtained Google ID Token:", idToken);

    // Using ngZone ensures Angular notices the API response and updates UI accordingly
    this.ngZone.run(() => {
      fetch('http://localhost:8080/api/auth/googleLogin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ idToken: idToken })
      })
        .then(res => {
          if (!res.ok) throw new Error('Backend authentication failed');
          return res.json();
        })
        .then(jwtResponse => {
          console.log("Success! Received JWT from backend:", jwtResponse);
          alert("Login successful!");
        })
        .catch(error => {
          console.error("Error during authentication:", error);
          alert("Login failed.");
        });
    });
  }
}

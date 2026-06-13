package com.arenic.backend.config.security;

import com.arenic.backend.config.security.dto.*;
import com.arenic.backend.modules.identity.internal.model.User;
import com.arenic.backend.modules.identity.internal.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    final AuthService authService;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.Response> login(@Valid @RequestBody AuthDto.LoginRequest request){
        return authService.loginUser(request.email(), request.password())
                .map(AuthDto.Response::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<AuthDto.Response> googleLogin(@Valid @RequestBody AuthDto.GoogleLoginRequest request){
        return authService.getTokenFromGoogle(request.idToken())
                .map(AuthDto.Response::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDto.Response> refreshToken(@Valid @RequestBody AuthDto.RefreshRequest request){
        return authService.refreshAccessToken(request.refreshToken())
                .map(AuthDto.Response::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody AuthDto.LogoutRequest request){
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

}

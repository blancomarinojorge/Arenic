package com.arenic.backend.config.security;

import com.arenic.backend.config.security.dto.JwtResponse;
import com.arenic.backend.config.security.dto.LoginRequest;
import com.arenic.backend.config.security.model.RefreshToken;
import com.arenic.backend.config.security.repository.RefreshTokenRepository;
import com.arenic.backend.config.security.token.TokenService;
import com.arenic.backend.modules.identity.internal.model.User;
import com.arenic.backend.modules.identity.internal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final TokenService tokenService;
    final RefreshTokenRepository refreshTokenRepository;
    final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){

        User newUser = new User();
        newUser.setEmail("your-email2@example.com");
        newUser.setName("Jorge");
        newUser.setSurname1("Blanco");
        newUser.setSurname2("");
        newUser.setPassword(passwordEncoder.encode("abc123.")); // Randomized dummy password
        userRepository.save(newUser);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email()).orElseThrow();
        return ResponseEntity.ok(buildAuthResponse(user));
    }

    private JwtResponse buildAuthResponse(User user) {
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getEmail());

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(UUID.randomUUID().toString()) //todo#12 gardar token cifrado
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        refreshTokenRepository.save(refreshToken);

        return new JwtResponse(accessToken, refreshToken.getToken(), user.getId(), user.getEmail());
    }
}

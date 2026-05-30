package com.arenic.backend.config.security;

import com.arenic.backend.config.security.dto.AuthDto;
import com.arenic.backend.config.security.model.RefreshToken;
import com.arenic.backend.config.security.repository.RefreshTokenRepository;
import com.arenic.backend.config.security.token.TokenService;
import com.arenic.backend.modules.identity.api.UserService;
import com.arenic.backend.modules.identity.internal.dto.CreateUserDto;
import com.arenic.backend.modules.identity.internal.model.User;
import com.arenic.backend.modules.identity.internal.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Transactional
    public Optional<AuthDto.TokensResult> loginUser(String email, String password){
        /* 1. Authenticate de user */
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User)
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(email, password))
                        .getPrincipal();
        log.debug("User authenticated: {}", userDetails.getUsername());

        /* 2. Get the full user object to create the token */
        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found in db: " + userDetails.getUsername()
                ));

        return Optional.of(generateTokens(user));
    }

    public Optional<AuthDto.TokensResult> getTokenFromGoogle(String idToken){
        try{
            /* 1. We get the user google information from the frontend token */
            GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singleton(googleClientId))
                    .build();

            GoogleIdToken accessToken = googleIdTokenVerifier.verify(idToken);
            if (accessToken == null){
                log.warn("Google ID Token verification failed (token was null or invalid)");
                return Optional.empty();
            }

            GoogleIdToken.Payload payload = accessToken.getPayload();
            String email = payload.getEmail();

            /*
            * We dont use @Transactional in the method since it would start the transaction
            * before doing the google api call, blocking a db connection while waiting for
            * the response.
            *
            * Instead, we wrap the db inserts into a transactionTemplate, making it much more
            * efficient in this case.
            * */
            return Optional.ofNullable(
                transactionTemplate.execute(status -> {

                    /* 2. In case the user does not exist in the database we create it*/
                    User user = userRepository
                            .findByEmail(email)
                            .orElseGet(() -> {
                                User newUser = User.builder()
                                        .email(payload.getEmail())
                                        .name((String) payload.get("given_name"))
                                        .surname1((String) payload.get("family_name"))
                                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                                        .build();

                                userRepository.save(newUser);
                                log.info("New user created via Google authentication. Email: {}", newUser.getEmail());
                                return newUser;
                            });

                    /* 3. Generate the tokenResult and reset the refresh token */
                    return generateTokens(user);

                })
            );
        }catch (Exception e){
            log.error("System error during Google OAuth flow {}", e.getMessage(), e);
            throw new AuthenticationServiceException("Internal authentication service error", e);
        }

    }

    @Transactional
    public Optional<AuthDto.TokensResult> refreshAccessToken(String refreshTokenStr){
        // 1. Get the refresh token, return if does not exists
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(refreshTokenStr);
        if (refreshTokenOptional.isEmpty()){
            log.warn("Refresh token attempt failed: Token not found in database.");
            return Optional.empty();
        }

        // 2. Check if the token has expired
        RefreshToken refreshToken = refreshTokenOptional.get();
        if (refreshToken.getExpiryDate().isBefore(Instant.now())){
            log.warn(
                    "Refresh token attempt failed: Token expired at {}. User ID: {}",
                    refreshToken.getExpiryDate(),
                    refreshToken.getUserId()
            );
            return Optional.empty();
        }

        // 3. Get the user related to the token
        User user = userRepository
                .getUserById(refreshToken.getUserId())
                .orElseThrow(() -> new IllegalStateException("Database inconsistency: User ID " + refreshToken.getUserId() + " owns a valid refresh token but does not exist."));


        // 4. Delete the refresh token
        refreshTokenRepository.delete(refreshToken);
        log.info("Successfully rotated refresh token for User ID: {}", user.getId());

        // 5. Generate the new tokens
        return Optional.of(generateTokens(user));
    }

    private AuthDto.TokensResult generateTokens(User user) {
        /* 1. Generate the JWT token */
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getEmail());

        /* 2. Generate a new refresh token */
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(UUID.randomUUID().toString()) //todo#12 we can look to encrypt the token, but for now we just save the plain uuid
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        refreshTokenRepository.save(refreshToken);

        return new AuthDto.TokensResult(accessToken, refreshToken.getToken(), user.getId(), user.getEmail());
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        refreshTokenRepository.findByToken(refreshTokenStr)
                .ifPresentOrElse(
                        refreshToken -> {
                            refreshTokenRepository.delete(refreshToken);
                            log.info("User logged out. Deleted refresh token for User ID: {}", refreshToken.getUserId());
                        },
                        () -> log.warn("Logout attempt with unknown refresh token.")
                );
    }
}

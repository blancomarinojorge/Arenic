package com.arenic.backend.config.security.dto;

import java.util.UUID;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDto {
    private AuthDto() {} // Namespace container

    // 1. Standard Login incoming payload
    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {}

    // 2. Google OAuth incoming payload
    public record GoogleLoginRequest(
            @NotBlank String idToken
    ) {}

    // 3. Token Refresh incoming payload
    public record RefreshRequest(
            @NotBlank String refreshToken
    ) {}

    // 4. Clean domain internal record for your AuthService business layer
    public record TokensResult(
            String accessToken,
            String refreshToken,
            UUID userId,
            String email
    ) {}

    // 5. Public API JSON response (The real object traveling over the network)
    public record Response(
            String accessToken,
            String refreshToken,
            UUID userId,
            String email
    ) {
        // Factory mapper method from your internal service domain data
        public static Response from(TokensResult result) {
            return new Response(
                    result.accessToken(),
                    result.refreshToken(),
                    result.userId(),
                    result.email()
            );
        }
    }

    // 6. Logout incoming payload
    public record LogoutRequest(
            @NotBlank String refreshToken
    ) {}

}
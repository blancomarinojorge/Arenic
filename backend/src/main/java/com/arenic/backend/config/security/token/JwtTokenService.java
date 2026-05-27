package com.arenic.backend.config.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService{

    final JwtEncoder jwtEncoder;

    @Override
    public String generateAccessToken(UUID userId, String userEmail) {
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("tennis-court-app")
                .issuedAt(Instant.now())
                .expiresAt(now.plus(15, ChronoUnit.MINUTES)) //todo#12 replace this with .env attribute
                .subject(userId.toString())
                .claim("email", userEmail)
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();
    }
}

package com.arenic.backend.config.security.token;

import java.util.UUID;

public interface TokenService {
    String generateAccessToken(UUID userId, String userEmail);
}

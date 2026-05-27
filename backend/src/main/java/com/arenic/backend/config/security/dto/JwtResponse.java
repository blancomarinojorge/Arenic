package com.arenic.backend.config.security.dto;

import java.util.UUID;

public record JwtResponse (String token, String refreshToken, UUID userId, String email) {}

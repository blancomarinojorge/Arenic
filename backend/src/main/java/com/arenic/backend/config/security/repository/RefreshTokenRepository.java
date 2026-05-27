package com.arenic.backend.config.security.repository;

import com.arenic.backend.config.security.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}

package com.arenic.backend.config.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_refresh_tokens") //todo#12 create table in postgress
@AllArgsConstructor @NoArgsConstructor @Builder @Getter @Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private Instant expiryDate;
}

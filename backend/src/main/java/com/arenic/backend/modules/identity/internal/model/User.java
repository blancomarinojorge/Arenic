package com.arenic.backend.modules.identity.internal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/*@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter*/
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private Integer phone;
    @Column(nullable = false)
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
}

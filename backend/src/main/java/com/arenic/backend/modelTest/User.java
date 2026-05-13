package com.arenic.backend.modelTest;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private boolean isActive = true;

    // Relationship for Ownership check
    @OneToMany(mappedBy = "creator")
    private Set<Club> ownedClubs;
}
package com.arenic.backend.modelTest;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "courts")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(nullable = false)
    private String name;

    @Column(name = "court_type")
    private String courtType;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    // Getters and Setters...
}
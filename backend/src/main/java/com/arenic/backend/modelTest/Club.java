package com.arenic.backend.modelTest;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "clubs")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "club")
    private Set<Court> courts;

    // Getters and Setters...
}
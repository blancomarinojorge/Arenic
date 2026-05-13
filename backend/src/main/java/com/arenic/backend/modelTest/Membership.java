package com.arenic.backend.modelTest;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "memberships")
public class Membership {
    @EmbeddedId
    private MembershipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clubId")
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(name = "assigned_role")
    private UserRole assignedRole;

    @Column(name = "joined_at")
    private OffsetDateTime joinedAt = OffsetDateTime.now();

    public enum UserRole { OWNER, EMPLOYEE, COACH, MEMBER }

    // Getters and Setters...
}
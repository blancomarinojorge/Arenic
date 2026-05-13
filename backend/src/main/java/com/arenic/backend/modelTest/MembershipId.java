package com.arenic.backend.modelTest;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class MembershipId implements Serializable {
    private UUID userId;
    private UUID clubId;

    public MembershipId() {}

    public MembershipId(UUID userId, UUID clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }

    // Getters, Setters, Equals, and HashCode
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getClubId() { return clubId; }
    public void setClubId(UUID clubId) { this.clubId = clubId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipId that = (MembershipId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(clubId, that.clubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clubId);
    }
}
package com.arenic.backend.modules.club.internal.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
public class ClubMembershipId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "club_id")
    private UUID clubId;
    @Column(name = "club_membership_role_slug")
    private String clubMembershipRole;
}

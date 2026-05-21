package com.arenic.backend.modules.club.internal.model;

import com.arenic.backend.modules.club.internal.model.enums.ClubMembershipRole;
import com.arenic.backend.modules.club.internal.model.pk.ClubMembershipId;
import com.arenic.backend.modules.identity.internal.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "club_memberships")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClubMembership {
    @EmbeddedId
    private ClubMembershipId id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "club_id")
    @MapsId("clubId")
    private Club club;

    // @MapsId("clubMembershipRole")
    /*
    * I dont put mapsid since it causes an issue with hibernate and enums in embedded keys
    * */
    @Column(name = "club_membership_role_slug", insertable = false, updatable = false)
    private ClubMembershipRole clubMembershipRole;
}

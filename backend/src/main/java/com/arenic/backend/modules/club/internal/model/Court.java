package com.arenic.backend.modules.club.internal.model;

import com.arenic.backend.common.model.BaseEntity;
import com.arenic.backend.modules.club.internal.model.enums.CourtType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "courts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Court extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club club;
    @Column(length = 100)
    private String name;
    @Column(name = "court_type")
    private CourtType courtType;
    private boolean isActive;
}

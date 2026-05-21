package com.arenic.backend.modules.club.internal.model.reference;

import com.arenic.backend.common.model.BaseReferenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "game_modes")
@Getter @Setter @SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameModeEntity extends BaseReferenceEntity {
    @Column(nullable = false)
    private Short numberOfPlayers;
}

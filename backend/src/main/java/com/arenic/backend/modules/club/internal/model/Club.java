package com.arenic.backend.modules.club.internal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

/*@Entity
@Table(name = "clubs")
@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter*/
public class Club {
    private UUID id;
    private String name;

}

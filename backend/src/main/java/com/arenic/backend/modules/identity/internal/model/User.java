package com.arenic.backend.modules.identity.internal.model;

import com.arenic.backend.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @NotBlank
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @NotBlank
    @Size(max = 255)
    @Column(name = "password", nullable = false)
    private String password;
    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank
    @Size(max = 255)
    @Column(name = "surname1", nullable = false)
    private String surname1;
    @Size(max = 255)
    @Column(name = "surname2")
    private String surname2;
}

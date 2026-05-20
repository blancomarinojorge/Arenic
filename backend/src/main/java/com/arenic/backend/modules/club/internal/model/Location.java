package com.arenic.backend.modules.club.internal.model;

import com.arenic.backend.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "locations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @NotBlank
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @NotBlank
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @NotBlank
    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;

    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String formattedAddress;

    @NotBlank
    private String timezone;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

}
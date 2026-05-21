package com.arenic.backend.modules.club.internal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "locations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Location {

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

    private BigDecimal latitude;
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String formattedAddress;

    @NotBlank
    private String timezone;

}
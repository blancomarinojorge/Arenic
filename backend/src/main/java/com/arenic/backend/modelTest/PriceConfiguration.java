package com.arenic.backend.modelTest;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "price_configurations")
public class PriceConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    private Court court;

    @Column(name = "hourly_rate", nullable = false)
    private BigDecimal hourlyRate;

    @Column(name = "valid_from", nullable = false)
    private OffsetDateTime validFrom;

    @Column(name = "valid_until")
    private OffsetDateTime validUntil;

    // Getters and Setters...
}
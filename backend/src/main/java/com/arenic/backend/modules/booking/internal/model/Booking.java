package com.arenic.backend.modules.booking.internal.model;

import com.arenic.backend.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter @Setter
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "court_id", nullable = false)
    private UUID courtId;

    @Column(name = "initiator_id")
    private UUID initiatorId;

    @Column(name = "game_mode", nullable = false)
    private String gameMode;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    @Column(name = "base_total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseTotalPrice;

    @Column(name = "final_total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalTotalPrice;

    @Column(name = "currency", nullable = false, length = 3)
    @JdbcTypeCode(Types.CHAR)
    private String currency = "EUR";

    @Column(name = "booking_status", nullable = false)
    private String bookingStatus;

    @Column(name = "booking_type", nullable = false)
    private String bookingType;
}

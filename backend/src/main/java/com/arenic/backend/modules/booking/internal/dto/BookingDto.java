package com.arenic.backend.modules.booking.internal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BookingDto {

    public record CreateRequest(
            @NotNull UUID courtId,
            @NotNull OffsetDateTime startTime,
            @NotNull OffsetDateTime endTime,
            @NotBlank String gameMode,
            @NotNull @Positive BigDecimal totalPrice
    ) {}

    public record CreateResponse(UUID bookingId) {}
}

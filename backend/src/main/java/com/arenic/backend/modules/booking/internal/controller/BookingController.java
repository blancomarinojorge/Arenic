package com.arenic.backend.modules.booking.internal.controller;

import com.arenic.backend.modules.booking.api.BookingService;
import com.arenic.backend.modules.booking.internal.dto.BookingDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto.CreateResponse> createBooking(
            @Valid @RequestBody BookingDto.CreateRequest request,
            Authentication authentication
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.createBooking(userId, request));
    }
}

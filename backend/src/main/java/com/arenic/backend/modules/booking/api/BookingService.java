package com.arenic.backend.modules.booking.api;

import com.arenic.backend.modules.booking.internal.dto.BookingDto;
import com.arenic.backend.modules.booking.internal.model.Booking;
import com.arenic.backend.modules.booking.internal.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public BookingDto.CreateResponse createBooking(UUID initiatorId, BookingDto.CreateRequest request) {
        Booking booking = new Booking();
        booking.setCourtId(request.courtId());
        booking.setInitiatorId(initiatorId);
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());
        booking.setGameMode(request.gameMode());
        booking.setBaseTotalPrice(request.totalPrice());
        booking.setFinalTotalPrice(request.totalPrice());
        booking.setCurrency("EUR");
        booking.setBookingStatus("confirmed");
        booking.setBookingType("private");

        Booking saved = bookingRepository.save(booking);
        return new BookingDto.CreateResponse(saved.getId());
    }
}

package com.arenic.backend.modules.booking.internal.repository;

import com.arenic.backend.modules.booking.internal.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
}

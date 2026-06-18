package com.arenic.backend.modules.booking.internal.repository;

import com.arenic.backend.modules.booking.internal.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT b FROM Booking b WHERE b.courtId IN :courtIds " +
           "AND b.startTime >= :dayStart AND b.startTime < :dayEnd " +
           "AND b.bookingStatus <> 'cancelled'")
    List<Booking> findActiveByCourtIdsInDay(
            @Param("courtIds") List<UUID> courtIds,
            @Param("dayStart") OffsetDateTime dayStart,
            @Param("dayEnd") OffsetDateTime dayEnd);
}

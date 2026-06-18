package com.arenic.backend.modules.club.api;

import com.arenic.backend.modules.booking.internal.model.Booking;
import com.arenic.backend.modules.booking.internal.repository.BookingRepository;
import com.arenic.backend.modules.club.internal.model.Club;
import com.arenic.backend.modules.club.internal.model.Court;
import com.arenic.backend.modules.club.internal.model.PriceRule;
import com.arenic.backend.modules.club.internal.model.PriceRuleInterval;
import com.arenic.backend.modules.club.internal.model.dto.ClubDto;
import com.arenic.backend.modules.club.internal.repository.ClubRepository;
import com.arenic.backend.modules.club.internal.repository.PriceRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubService{
    final ClubRepository clubRepository;
    final PriceRuleRepository priceRuleRepository;
    final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public ClubDto.SearchResponse search(String query){
        if (query == null || query.length() < 2){
            return new ClubDto.SearchResponse(List.of(), List.of());
        }

        /* 1. Get the clubs by name or city */
        List<Club> searchResults = clubRepository.searchByNameOrCity(query);

        /* 2. Get the cities data */
        List<ClubDto.CityResult> cityResults = searchResults.stream()
                .map(club -> club.getLocation().getCity())
                .distinct()
                .map(city -> {
                    return new ClubDto.CityResult(
                            city,
                            searchResults.stream()
                                    .filter(club -> club.getLocation().getCity().toLowerCase().equals(city.toLowerCase()))
                                    .count()
                    );
                })
                .toList();

        /* 3. Paste the clubs to the dto */
        List<ClubDto.Summary> summaries = searchResults.stream()
                .map(ClubDto.Summary::from)
                .toList();

        return new ClubDto.SearchResponse(cityResults, summaries);
    }

    @Transactional(readOnly = true)
    public Optional<ClubDto.Detail> findById(UUID id) {
        return clubRepository.findById(id).map(ClubDto.Detail::from);
    }

    @Transactional(readOnly = true)
    public List<ClubDto.SlotEntry> getSlots(UUID clubId, LocalDate date) {
        Optional<Club> clubOpt = clubRepository.findById(clubId);
        if (clubOpt.isEmpty()) return List.of();

        Club club = clubOpt.get();
        int dayOfWeek = date.getDayOfWeek().getValue(); // 1=Mon, 7=Sun

        // Build set of already-booked "courtId:HH:00" keys for this day
        ZoneId clubZone = ZoneId.of(club.getLocation().getTimezone());
        ZonedDateTime zonedDayStart = date.atStartOfDay(clubZone);
        OffsetDateTime dayStart = zonedDayStart.toOffsetDateTime();
        OffsetDateTime dayEnd = zonedDayStart.plusDays(1).toOffsetDateTime();

        List<UUID> courtIds = club.getCourts().stream()
                .filter(Court::isActive)
                .map(Court::getId)
                .toList();

        Set<String> bookedKeys = new HashSet<>();
        if (!courtIds.isEmpty()) {
            List<Booking> existingBookings = bookingRepository.findActiveByCourtIdsInDay(courtIds, dayStart, dayEnd);
            for (Booking b : existingBookings) {
                LocalTime startLocal = b.getStartTime().atZoneSameInstant(clubZone).toLocalTime();
                bookedKeys.add(b.getCourtId() + ":" + startLocal);
            }
        }

        List<PriceRule> rules = priceRuleRepository.findByClubId(clubId);
        List<ClubDto.SlotEntry> result = new ArrayList<>();

        for (Court court : club.getCourts()) {
            if (!court.isActive()) continue;

            for (PriceRule rule : rules) {
                if (!rule.getCourtIds().contains(court.getId())) continue;
                if (!rule.getWeekDays().contains((short) dayOfWeek)) continue;

                for (PriceRuleInterval interval : rule.getIntervals()) {
                    if (interval.getIntervalMinutes() != 60) continue;

                    LocalTime cursor = rule.getStartTime();
                    while (!cursor.plusMinutes(60).isAfter(rule.getEndTime())) {
                        LocalTime slotEnd = cursor.plusMinutes(60);
                        BigDecimal memberPrice = interval.getTotalPrice()
                                .multiply(BigDecimal.valueOf(
                                        1.0 - interval.getMemberDiscountPercent() / 100.0))
                                .setScale(2, RoundingMode.HALF_UP);

                        boolean isBooked = bookedKeys.contains(court.getId() + ":" + cursor);

                        result.add(new ClubDto.SlotEntry(
                                court.getId(),
                                cursor.toString(),
                                slotEnd.toString(),
                                60,
                                interval.getTotalPrice(),
                                memberPrice,
                                interval.getCurrency(),
                                interval.getGameMode(),
                                isBooked
                        ));
                        cursor = slotEnd;
                    }
                }
            }
        }

        return result;
    }
}

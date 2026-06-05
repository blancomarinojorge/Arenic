package com.arenic.backend.modules.club.internal.model.dto;

import com.arenic.backend.modules.club.internal.model.Club;
import com.arenic.backend.modules.club.internal.model.Court;

import java.util.List;
import java.util.UUID;

public class ClubDto {
    public record Summary(
            UUID id,
            String name,
            String city,
            String addressLine1,
            int courtCount
    ) {
        public static Summary from(Club club) {
            return new Summary(
                    club.getId(),
                    club.getName(),
                    club.getLocation().getCity(),
                    club.getLocation().getAddressLine1(),
                    club.getCourts().size()
            );
        }
    }

    public record CourtSummary(
            UUID id,
            String name,
            String courtType
    ) {
        public static CourtSummary from(Court court) {
            return new CourtSummary(
                    court.getId(),
                    court.getName(),
                    court.getCourtType().getSlug()
            );
        }
    }

    public record Detail(
            UUID id,
            String name,
            String city,
            String addressLine1,
            String addressLine2,
            String zipCode,
            List<CourtSummary> courts
    ) {
        public static Detail from(Club club) {
            return new Detail(
                    club.getId(),
                    club.getName(),
                    club.getLocation().getCity(),
                    club.getLocation().getAddressLine1(),
                    club.getLocation().getAddressLine2(),
                    club.getLocation().getZipCode(),
                    club.getCourts().stream()
                            .filter(Court::isActive)
                            .map(CourtSummary::from)
                            .toList()
            );
        }
    }

    public record CityResult(
            String city,
            long clubCount
    ){}

    public record SearchResponse(
            List<CityResult> cities,
            List<Summary> clubs
    ){}
}

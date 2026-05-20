package com.arenic.backend.modules.club.internal.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CourtType {
    PADEL("PADEL"),
    TENNIS_GRASS("TENNIS_GRASS"),
    TENNIS_HARD("TENNIS_HARD"),
    PICKLEBALL("PICKLEBALL");

    private final String slug;

    public static CourtType fromSlug(String slug) {
        return Stream.of(values())
                .filter(t -> t.slug.equals(slug))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown CourtType slug: " + slug));
    }
}
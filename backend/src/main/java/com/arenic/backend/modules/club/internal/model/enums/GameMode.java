package com.arenic.backend.modules.club.internal.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum GameMode {
    SINGLES("SINGLES", 2),
    DOUBLES("DOUBLES", 4);

    private final String slug;
    private final int numberOfPlayers;

    public static GameMode fromSlug(String slug) {
        return Stream.of(values())
                .filter(g -> g.slug.equals(slug))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown GameMode: " + slug));
    }
}

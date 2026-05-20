package com.arenic.backend.modules.club.internal.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClubMembershipRole {
    OWNER("OWNER"),
    MANAGER("MANAGER"),
    STAFF("STAFF"),
    PLAYER("PLAYER");

    private final String slug;

    public static ClubMembershipRole fromSlug(String slug) {
        return Stream.of(values())
                .filter(r -> r.slug.equals(slug))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ClubMembershipRole slug: " + slug));
    }
}
package com.arenic.backend.common.converter;

import com.arenic.backend.modules.club.internal.model.enums.ClubMembershipRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ClubMembershipRoleConverter implements AttributeConverter<ClubMembershipRole, String> {
    @Override
    public String convertToDatabaseColumn(ClubMembershipRole gameMode) {
        return gameMode.getSlug();
    }

    @Override
    public ClubMembershipRole convertToEntityAttribute(String s) {
        return ClubMembershipRole.fromSlug(s);
    }
}

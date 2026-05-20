package com.arenic.backend.common.converter;

import com.arenic.backend.modules.club.internal.model.enums.CourtType;
import com.arenic.backend.modules.club.internal.model.enums.GameMode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CourtTypeConverter implements AttributeConverter<CourtType, String> {
    @Override
    public String convertToDatabaseColumn(CourtType gameMode) {
        return gameMode.getSlug();
    }

    @Override
    public CourtType convertToEntityAttribute(String s) {
        return CourtType.fromSlug(s);
    }
}

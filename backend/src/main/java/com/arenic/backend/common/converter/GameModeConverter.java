package com.arenic.backend.common.converter;

import com.arenic.backend.modules.club.internal.model.enums.GameMode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GameModeConverter implements AttributeConverter<GameMode, String> {
    @Override
    public String convertToDatabaseColumn(GameMode gameMode) {
        return gameMode.getSlug();
    }

    @Override
    public GameMode convertToEntityAttribute(String s) {
        return GameMode.fromSlug(s);
    }
}

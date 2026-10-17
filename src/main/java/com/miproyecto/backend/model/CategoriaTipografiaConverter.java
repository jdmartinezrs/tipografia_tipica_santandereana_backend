package com.miproyecto.backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoriaTipografiaConverter implements AttributeConverter<CategoriaTipografia, String> {

    @Override
    public String convertToDatabaseColumn(CategoriaTipografia categoria) {
        if (categoria == null) {
            return null;
        }
        return categoria.getDisplayName();
    }

    @Override
    public CategoriaTipografia convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return CategoriaTipografia.fromDisplayName(dbData);
    }
}

package com.example.dividend.converter;

import com.example.dividend.model.enums.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class AuthorityConverter implements AttributeConverter<List<Authority>, String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(List<Authority> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<Authority> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(Authority::valueOf)
                .collect(Collectors.toList());
    }
}
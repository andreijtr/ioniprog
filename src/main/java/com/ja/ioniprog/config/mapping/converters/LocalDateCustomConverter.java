package com.ja.ioniprog.config.mapping.converters;

import com.ja.ioniprog.utils.enums.ApplicationEnum;
import org.dozer.CustomConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateCustomConverter implements CustomConverter {
    @Override
    public Object convert(Object destination, Object source, Class<?> destClass, Class<?> sourceClass) {
        if (source == null)
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATE_FORMATTER.getName());

        if (source instanceof String) {
            String dateAsString = (String) source;
            return LocalDate.parse(dateAsString, formatter);
        }
        else if (source instanceof LocalDate) {
            LocalDate date = (LocalDate) source;
            return date.format(formatter);
        }

        return null;
    }
}

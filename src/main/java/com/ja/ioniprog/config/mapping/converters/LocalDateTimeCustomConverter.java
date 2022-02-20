package com.ja.ioniprog.config.mapping.converters;

import com.ja.ioniprog.utils.enums.ApplicationEnum;
import org.dozer.CustomConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeCustomConverter implements CustomConverter {
    @Override
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null)
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationEnum.DATETIME_FORMATTER.getName());

        if (source instanceof String) {
            String dateTimeString = (String) source;
            return LocalDateTime.parse(dateTimeString, formatter);
        }

        if (source instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) source;
            return dateTime.format(formatter);
        }

        return null;
    }
}

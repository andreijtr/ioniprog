package com.ja.ioniprog.config.mapping.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.ioniprog.model.dto.audit.ChangeDto;
import org.dozer.CustomConverter;

import java.util.ArrayList;
import java.util.List;

public class ChangesCustomConverter implements CustomConverter {
    private ObjectMapper objectMapper;

    public ChangesCustomConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Object convert(Object destination, Object source, Class<?> destClass, Class<?> sourceClass) {
        if (source == null)
            return null;

        if (source instanceof String) {
            String changesAsString = (String) source;
            List<ChangeDto> changes = new ArrayList<>();
            try {
                changes = objectMapper.readValue(changesAsString, new TypeReference<List<ChangeDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return changes;
        }
        else if (source instanceof List) {
            List<ChangeDto> changes = (List<ChangeDto>) source;
            String changesAsString = null;
            try {
                changesAsString = objectMapper.writeValueAsString(changes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return changesAsString;
        }

        return null;
    }
}

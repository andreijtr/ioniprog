package com.ja.ioniprog.utils.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class JsonParser {

    private ObjectMapper objectMapper;

    @Autowired
    public JsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getJson(Object object) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}

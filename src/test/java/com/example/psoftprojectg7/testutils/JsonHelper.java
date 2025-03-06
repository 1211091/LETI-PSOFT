package com.example.psoftprojectg7.testutils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static <T> String toJson(final ObjectMapper objectMapper, final T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T fromJson(final ObjectMapper objectMapper, final String payload, final Class<T> clazz)
            throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }

    public static <T> T fromJson(final ObjectMapper objectMapper, final String payload, final TypeReference<T> clazz)
            throws JsonProcessingException {
        return objectMapper.readValue(payload, clazz);
    }

}

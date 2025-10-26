package com.example.slabiak.appointmentscheduler.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeArrayDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isArray()) {
            // Se for um array [hora, minuto]
            int hour = node.get(0).asInt();
            int minute = node.get(1).asInt();
            return LocalTime.of(hour, minute);
        } else if (node.isTextual()) {
            // Se for uma string "hh:mm" (para dados novos ou de outras partes do app)
            return LocalTime.parse(node.asText());
        }

        return null;
    }
}
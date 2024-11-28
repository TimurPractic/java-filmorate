package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.yandex.practicum.filmorate.model.Rating;

public class RatingDeserializer extends StdDeserializer<Rating> {

    public RatingDeserializer() {
        super(Rating.class);
    }

    @Override
    public Rating deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isObject()) {
            // Получаем поля id и name из объекта
            int id = node.get("id").asInt();
            // Преобразуем id в Rating
            return Rating.fromId(id);  // Используем метод fromId
        } else {
            throw new IllegalArgumentException("Invalid Rating format");
        }
    }
}

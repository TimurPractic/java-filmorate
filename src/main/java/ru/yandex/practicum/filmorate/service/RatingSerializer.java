package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import ru.yandex.practicum.filmorate.model.Rating;

public class RatingSerializer extends JsonSerializer<Rating> {

    @Override
    public void serialize(Rating value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());          // Сериализуем id
        gen.writeStringField("name", value.getDisplayName());  // Сериализуем name
        gen.writeEndObject();
    }
}

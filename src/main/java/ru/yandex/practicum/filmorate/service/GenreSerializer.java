package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.yandex.practicum.filmorate.model.Genre;

import java.io.IOException;
import java.util.List;

public class GenreSerializer extends JsonSerializer<List<Genre>> {
    @Override
    public void serialize(List<Genre> genres, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (Genre genre : genres) {
            gen.writeStartObject();
            gen.writeNumberField("id", genre.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}

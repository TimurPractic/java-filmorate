package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.yandex.practicum.filmorate.model.Genre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenreDeserializer extends JsonDeserializer<List<Genre>> {
    @Override
    public List<Genre> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Genre> genres = new ArrayList<>();
        JsonNode arrayNode = parser.getCodec().readTree(parser);

        if (!arrayNode.isArray()) {
            throw new IllegalArgumentException("Поле 'genres' должно быть массивом. Получено: " + arrayNode);
        }

        for (JsonNode node : arrayNode) {
            JsonNode idNode = node.get("id");
            if (idNode == null || !idNode.isInt()) {
                throw new IllegalArgumentException("Поле 'id' отсутствует или некорректно в жанре: " + node);
            }

            int id = idNode.asInt();
            Genre genre = Genre.fromId(id); // Метод fromId должен быть реализован в Genre
            genres.add(genre);
        }
        return genres;
    }
}

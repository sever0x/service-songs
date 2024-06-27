package com.sever0x.block2.parser.json.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.sever0x.block2.validation.ValidationUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ParsedSong(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Artist name cannot be blank")
        String artistName,
        @NotBlank(message = "Artist country cannot be blank")
        String artistCountry,
        @NotBlank(message = "Album cannot be blank")
        String album,
        @NotBlank(message = "Genres cannot be blank")
        String genres,
        @Positive(message = "Duration must be positive")
        int duration,
        @Positive(message = "Release year must be positive")
        int releaseYear
) {
    public ParsedSong(JsonNode jsonNode) {
        this(
                jsonNode.get("title").asText(),
                jsonNode.get("artist").get("name").asText(),
                jsonNode.get("artist").get("country").asText(),
                jsonNode.get("album").asText(),
                jsonNode.get("genres").asText(),
                jsonNode.get("duration").asInt(),
                jsonNode.get("releaseYear").asInt()
        );
        ValidationUtils.validate(this);
    }
}

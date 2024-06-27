package com.sever0x.block2.parser.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block2.parser.json.response.JsonParseResponse;
import com.sever0x.block2.parser.json.response.ParsedSong;
import com.sever0x.block2.validation.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JsonPlaylistParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonParseResponse parseSongsFromFile(InputStream inputStream) throws IOException {
        List<ParsedSong> parsedSongs = new ArrayList<>();
        JsonFactory jsonFactory = new JsonFactory();

        int failures = 0;

        try (JsonParser jsonParser = jsonFactory.createParser(inputStream)) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Root node is not an array");
            }

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                JsonNode rootNode = mapper.readTree(jsonParser);
                try {
                    parsedSongs.add(new ParsedSong(rootNode));
                } catch (ValidationException e) {
                    failures++;
                    log.error("Skipping invalid record: " + e.getMessage());
                }
            }
        }

        return new JsonParseResponse(parsedSongs, failures);
    }
}
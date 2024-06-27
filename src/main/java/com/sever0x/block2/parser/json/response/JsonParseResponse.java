package com.sever0x.block2.parser.json.response;

import java.util.List;

public record JsonParseResponse(
        List<ParsedSong> parsedSongs,
        int missedSongs
) {
}

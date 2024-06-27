package com.sever0x.block2.model.dto.response;

import lombok.Builder;

@Builder
public record SongResponse(
        long id,
        String title,
        ArtistResponse artist,
        String album,
        String genres,
        int duration,
        int releaseYear
) {
}

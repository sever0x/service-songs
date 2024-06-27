package com.sever0x.block2.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetSongsResponse(
        List<ShortSongResponse> list,
        int totalPages
) {
}

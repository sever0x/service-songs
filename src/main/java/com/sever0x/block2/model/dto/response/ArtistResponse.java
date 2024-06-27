package com.sever0x.block2.model.dto.response;

import lombok.Builder;

@Builder
public record ArtistResponse(
        long id,
        String name,
        String country
) {
}

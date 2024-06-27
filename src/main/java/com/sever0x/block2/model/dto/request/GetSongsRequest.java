package com.sever0x.block2.model.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetSongsRequest(
        Long artistId,
        String album,
        @PositiveOrZero
        int page,
        @Positive
        int size
) {
}

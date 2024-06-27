package com.sever0x.block2.model.dto.request;

import com.sever0x.block2.validation.annotation.AtLeastOneNotNull;

@AtLeastOneNotNull
public record GenerateReportSongsRequest(
        Long artistId,
        String album
) {
}

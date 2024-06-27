package com.sever0x.block2.model.dto.response;

import java.io.ByteArrayInputStream;

public record GenerateReportSongsResponse(
        String name,
        ByteArrayInputStream report
) {
}

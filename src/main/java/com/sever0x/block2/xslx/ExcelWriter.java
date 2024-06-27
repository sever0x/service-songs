package com.sever0x.block2.xslx;

import com.sever0x.block2.model.entity.Song;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ExcelWriter {

    void createExcelReport(List<Song> songs, ByteArrayOutputStream out);
}

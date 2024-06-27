package com.sever0x.block2.xslx;

import com.sever0x.block2.model.entity.Song;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelWriterImpl implements ExcelWriter {

    @Override
    public void createExcelReport(List<Song> songs, ByteArrayOutputStream out) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Songs Report");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Title");
            headerRow.createCell(1).setCellValue("Artist");
            headerRow.createCell(2).setCellValue("Album");
            headerRow.createCell(3).setCellValue("Genres");
            headerRow.createCell(4).setCellValue("Duration");
            headerRow.createCell(5).setCellValue("Release Year");

            int rowNum = 1;
            for (Song song : songs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(song.getTitle());
                row.createCell(1).setCellValue(song.getArtist().getName());
                row.createCell(2).setCellValue(song.getAlbum());
                row.createCell(3).setCellValue(song.getGenres());
                row.createCell(4).setCellValue(song.getDuration());
                row.createCell(5).setCellValue(song.getReleaseYear());
            }

            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

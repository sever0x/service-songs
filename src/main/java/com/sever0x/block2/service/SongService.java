package com.sever0x.block2.service;

import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {

    SongResponse createSong(SongRequest request);

    SongResponse getSongById(long id);

    SongResponse updateSongById(long id, SongRequest request);

    DeleteSongResponse deleteSongById(long id);

    GetSongsResponse getSongs(GetSongsRequest request);

    GenerateReportSongsResponse generateReportSongs(GenerateReportSongsRequest request);

    UploadResponse importSongsFromFile(MultipartFile file);
}

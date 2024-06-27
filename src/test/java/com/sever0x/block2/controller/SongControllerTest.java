package com.sever0x.block2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.*;
import com.sever0x.block2.service.SongService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SongController.class)
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    @Test
    void shouldCreateSong() throws Exception {
        SongRequest request = new SongRequest("Test Song", 1L, "Test Album", "Rock,Pop", 180, 2022);
        SongResponse response = new SongResponse(1L, "Test Song", new ArtistResponse(1L, "Test Artist", "USA"), "Test Album", "Rock,Pop", 180, 2022);

        when(songService.createSong(any(SongRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.artist.id").value(1L))
                .andExpect(jsonPath("$.artist.name").value("Test Artist"))
                .andExpect(jsonPath("$.artist.country").value("USA"))
                .andExpect(jsonPath("$.album").value("Test Album"))
                .andExpect(jsonPath("$.genres").value("Rock,Pop"))
                .andExpect(jsonPath("$.duration").value(180))
                .andExpect(jsonPath("$.releaseYear").value(2022));

        verify(songService).createSong(request);
    }

    @Test
    void shouldFailToCreateSongWithBlankTitle() throws Exception {
        SongRequest request = new SongRequest("", 1L, "Test Album", "Rock,Pop", 180, 2022);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToCreateSongWithBlankAlbum() throws Exception {
        SongRequest request = new SongRequest("Test Song", 1L, "", "Rock,Pop", 180, 2022);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToCreateSongWithBlankGenres() throws Exception {
        SongRequest request = new SongRequest("Test Song", 1L, "Test Album", "", 180, 2022);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToCreateSongWithInvalidDuration() throws Exception {
        SongRequest request = new SongRequest("Test Song", 1L, "Test Album", "Rock,Pop", 0, 2022);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToCreateSongWithNegativeReleaseYear() throws Exception {
        SongRequest request = new SongRequest("Test Song", 1L, "Test Album", "Rock,Pop", 180, -2022);

        mockMvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetSongDetails() throws Exception {
        SongResponse response = new SongResponse(1L, "Test Song", new ArtistResponse(1L, "Test Artist", "USA"), "Test Album", "Rock,Pop", 180, 2022);

        when(songService.getSongById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/song/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Song"))
                .andExpect(jsonPath("$.artist.id").value(1L))
                .andExpect(jsonPath("$.artist.name").value("Test Artist"))
                .andExpect(jsonPath("$.artist.country").value("USA"))
                .andExpect(jsonPath("$.album").value("Test Album"))
                .andExpect(jsonPath("$.genres").value("Rock,Pop"))
                .andExpect(jsonPath("$.duration").value(180))
                .andExpect(jsonPath("$.releaseYear").value(2022));

        verify(songService).getSongById(1L);
    }

    @Test
    void shouldFailToGetNonExistingSongDetails() throws Exception {
        when(songService.getSongById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/song/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateSong() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "Updated Album", "Rock", 200, 2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(songService).updateSongById(1L, request);
    }

    @Test
    void shouldFailToUpdateSongWithBlankTitle() throws Exception {
        SongRequest request = new SongRequest("", 1L, "Updated Album", "Rock", 200, 2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateSongWithBlankAlbum() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "", "Rock", 200, 2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateSongWithBlankGenres() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "Updated Album", "", 200, 2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateSongWithInvalidDuration() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "Updated Album", "Rock", 0, 2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateSongWithNegativeReleaseYear() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "Updated Album", "Rock", 200, -2021);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToUpdateNonExistingSong() throws Exception {
        SongRequest request = new SongRequest("Updated Song", 1L, "Updated Album", "Rock", 200, 2021);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(songService)
                .updateSongById(1L, request);

        mockMvc.perform(put("/api/song/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteSong() throws Exception {
        DeleteSongResponse response = new DeleteSongResponse(1, true);
        when(songService.deleteSongById(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/song/1"))
                .andExpect(status().isOk());

        verify(songService).deleteSongById(1L);
    }

    @Test
    void shouldFailToDeleteNonExistingSong() throws Exception {
        when(songService.deleteSongById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/song/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetSongs() throws Exception {
        GetSongsRequest request = new GetSongsRequest(1L, "Test Album", 0, 10);
        ShortSongResponse response1 = new ShortSongResponse(1, "Test Song 1", "Test Artist", "Test Album", 180);
        ShortSongResponse response2 = new ShortSongResponse(2, "Test Song 2", "Test Artist", "Test Album", 200);
        GetSongsResponse getSongsResponse = new GetSongsResponse(List.of(response1, response2), 1);

        when(songService.getSongs(any(GetSongsRequest.class))).thenReturn(getSongsResponse);

        mockMvc.perform(post("/api/song/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(2))
                .andExpect(jsonPath("$.list[0].title").value("Test Song 1"))
                .andExpect(jsonPath("$.list[0].artistName").value("Test Artist"))
                .andExpect(jsonPath("$.list[0].album").value("Test Album"))
                .andExpect(jsonPath("$.list[0].duration").value(180))
                .andExpect(jsonPath("$.list[1].title").value("Test Song 2"))
                .andExpect(jsonPath("$.list[1].artistName").value("Test Artist"))
                .andExpect(jsonPath("$.list[1].album").value("Test Album"))
                .andExpect(jsonPath("$.list[1].duration").value(200))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(songService).getSongs(request);
    }

    @Test
    void shouldFailToGetSongsWithInvalidPageSize() throws Exception {
        GetSongsRequest request = new GetSongsRequest(1L, "Test Album", 0, 0);

        mockMvc.perform(post("/api/song/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGenerateReportSongs() throws Exception {
        GenerateReportSongsRequest request = new GenerateReportSongsRequest(1L, "Test Album");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        String fileName = "report_by_artistId_and_album.xlsx";

        when(songService.generateReportSongs(request)).thenReturn(new GenerateReportSongsResponse(fileName, new ByteArrayInputStream(outStream.toByteArray())));

        mockMvc.perform(post("/api/song/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + fileName + "\""))
                .andExpect(content().bytes(outStream.toByteArray()));

        verify(songService).generateReportSongs(request);
    }

    @Test
    void shouldFailToGenerateReportSongsWithoutAnyFilter() throws Exception {
        GenerateReportSongsRequest request = new GenerateReportSongsRequest(null, null);

        mockMvc.perform(post("/api/song/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUploadValidSongs() throws Exception {
        File validJsonFile = new File("src/test/resources/json/valid.json");
        MockMultipartFile file = new MockMultipartFile("file", validJsonFile.getName(), "application/json", Files.readAllBytes(validJsonFile.toPath()));
        UploadResponse response = new UploadResponse(4, 0);

        when(songService.importSongsFromFile(any(MultipartFile.class))).thenReturn(response);

        mockMvc.perform(multipart("/api/song/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(4))
                .andExpect(jsonPath("$.failureCount").value(0));

        verify(songService).importSongsFromFile(any(MultipartFile.class));
    }

    @Test
    void shouldUploadPartiallyValidSongs() throws Exception {
        File partiallyValidJsonFile = new File("src/test/resources/json/partially_valid.json");
        MockMultipartFile file = new MockMultipartFile("file", partiallyValidJsonFile.getName(), "application/json", Files.readAllBytes(partiallyValidJsonFile.toPath()));
        UploadResponse response = new UploadResponse(1, 3);

        when(songService.importSongsFromFile(any(MultipartFile.class))).thenReturn(response);

        mockMvc.perform(multipart("/api/song/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(1))
                .andExpect(jsonPath("$.failureCount").value(3));

        verify(songService).importSongsFromFile(any(MultipartFile.class));
    }
}
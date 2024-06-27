package com.sever0x.block2.controller;

import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.*;
import com.sever0x.block2.service.SongService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    /**
     * Creates a new song based on the provided request data.
     *
     * @param request the request containing the song data
     * @return the response containing the created song details
     */
    @PostMapping
    public SongResponse createSong(@RequestBody @Valid SongRequest request) {
        return songService.createSong(request);
    }

    /**
     * Retrieves the details of a song by its ID.
     *
     * @param id the ID of the song to retrieve
     * @return the response containing the song details
     */
    @GetMapping("/{id}")
    public SongResponse getSongDetails(@PathVariable long id) {
        return songService.getSongById(id);
    }

    /**
     * Updates an existing song with the provided request data.
     *
     * @param id      the ID of the song to update
     * @param request the request containing the updated song data
     */
    @PutMapping("/{id}")
    public SongResponse updateSong(@PathVariable long id, @RequestBody @Valid SongRequest request) {
        return songService.updateSongById(id, request);
    }

    /**
     * Deletes a song by its ID.
     *
     * @param id the ID of the song to delete
     * @return true if the song was deleted successfully, false otherwise
     */
    @DeleteMapping("/{id}")
    public DeleteSongResponse deleteSong(@PathVariable long id) {
        return songService.deleteSongById(id);
    }

    /**
     * Retrieves a list of songs based on the provided request data.
     *
     * @param request the request containing the filtering and pagination options
     * @return the response containing the list of songs and total pages
     */
    @PostMapping("/_list")
    public GetSongsResponse getSongs(@RequestBody @Valid GetSongsRequest request) {
        return songService.getSongs(request);
    }

    /**
     * Generates an Excel report of songs based on the provided request data.
     *
     * @param request the request containing the filtering options for the report
     * @return the response entity containing the generated report
     */
    @PostMapping(value = "/_report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> generateReportSongs(@RequestBody @Valid GenerateReportSongsRequest request,
                                                        HttpServletResponse servletResponse) {
        GenerateReportSongsResponse response = songService.generateReportSongs(request);
        InputStreamResource resource = new InputStreamResource(response.report());

        servletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        servletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + response.name() + "\"");
        servletResponse.setContentLength(response.report().available());

        return ResponseEntity.ok()
                .body(resource);
    }

    /**
     * Imports songs from a JSON file.
     *
     * @param file the MultipartFile containing the JSON data
     * @return the response containing the count of successfully imported songs and missed songs
     */
    @PostMapping("/upload")
    public UploadResponse uploadSongs(@RequestParam("file") MultipartFile file) {
        return songService.importSongsFromFile(file);
    }
}

package com.sever0x.block2.service.impl;

import com.sever0x.block2.mapper.SongMapper;
import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.*;
import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.model.entity.Song;
import com.sever0x.block2.parser.json.JsonPlaylistParser;
import com.sever0x.block2.parser.json.response.JsonParseResponse;
import com.sever0x.block2.parser.json.response.ParsedSong;
import com.sever0x.block2.repository.ArtistRepository;
import com.sever0x.block2.repository.SongRepository;
import com.sever0x.block2.service.SongService;
import com.sever0x.block2.xslx.ExcelWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private static final String EXCEL_REPORT_NAME = "report_by_%s.xlsx";

    private final ExcelWriter writer;

    private final SongMapper songMapper;

    private final SongRepository songRepository;

    private final ArtistRepository artistRepository;

    private final JsonPlaylistParser jsonPlaylistParser;

    /**
     * Creates a new song based on the provided request data.
     *
     * @param request the request containing the song data
     * @return the response containing the created song details
     */
    @Override
    @Transactional
    public SongResponse createSong(SongRequest request) {
        Song song = songMapper.requestToEntity(request);
        song.setArtist(getArtistOrThrow(request.artistId()));
        return songMapper.entityToResponse(songRepository.save(song));
    }

    /**
     * Retrieves the details of a song by its ID.
     *
     * @param id the ID of the song to retrieve
     * @return the response containing the song details
     * @throws ResponseStatusException if the song with the given ID is not found
     */
    @Override
    @Transactional(readOnly = true)
    public SongResponse getSongById(long id) {
        return songMapper.entityToResponse(getSongOrThrow(id));
    }

    /**
     * Updates an existing song with the provided request data.
     *
     * @param id      the ID of the song to update
     * @param request the request containing the updated song data
     * @throws ResponseStatusException if the song with the given ID is not found
     */
    @Override
    @Transactional
    public SongResponse updateSongById(long id, SongRequest request) {
        Song updatableSong = getSongOrThrow(id);
        updateSongFromRequest(updatableSong, request);
        return songMapper.entityToResponse(songRepository.save(updatableSong));
    }

    /**
     * Deletes a song by its ID.
     *
     * @param id the ID of the song to delete
     * @return true if the song was deleted successfully, false otherwise
     * @throws ResponseStatusException if the song with the given ID is not found
     */
    @Override
    @Transactional
    public DeleteSongResponse deleteSongById(long id) {
        if (!songRepository.existsById(id)) {
            throw getResponseStatusExceptionNotFound("Song with ID ", id);
        }
        songRepository.deleteById(id);
        return new DeleteSongResponse(id, true);
    }

    /**
     * Retrieves a list of songs based on the provided request data.
     *
     * @param request the request containing the filtering and pagination options
     * @return the response containing the list of songs and total pages
     */
    @Override
    @Transactional(readOnly = true)
    public GetSongsResponse getSongs(GetSongsRequest request) {
        Page<Song> songs = songRepository.findAll(getSongsPageable(request), request.artistId(), request.album());
        return GetSongsResponse.builder()
                .list(songs.map(songMapper::toShortResponse).stream().toList())
                .totalPages(songs.getTotalPages())
                .build();
    }

    /**
     * Generates an Excel report of songs based on the provided request data.
     *
     * @param request the request containing the filtering options for the report
     * @return the response containing the name and content of the generated report
     */
    @Override
    public GenerateReportSongsResponse generateReportSongs(GenerateReportSongsRequest request) {
        List<Song> songs = songRepository.findAll(
                PageRequest.of(0, Integer.MAX_VALUE), request.artistId(), request.album()
        ).getContent();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.createExcelReport(songs, out);

        String fileName = formatExcelReportName(request.artistId(), request.album());

        return new GenerateReportSongsResponse(fileName, new ByteArrayInputStream(out.toByteArray()));
    }

    /**
     * Imports songs from a JSON file.
     *
     * @param file the MultipartFile containing the JSON data
     * @return the response containing the count of successfully imported songs and missed songs
     * @throws RuntimeException if there is an error reading the file
     */
    @Override
    @Transactional
    public UploadResponse importSongsFromFile(MultipartFile file) {
        try {
            JsonParseResponse response = jsonPlaylistParser.parseSongsFromFile(file.getInputStream());
            List<ParsedSong> parsedSongs = response.parsedSongs();

            for (ParsedSong parsedSong : parsedSongs) {
                Song song = createSongFromParsedSong(parsedSong);
                songRepository.save(song);
            }

            return new UploadResponse(parsedSongs.size(), response.missedSongs());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private Song createSongFromParsedSong(ParsedSong parsedSong) {
        Artist artist = artistRepository.findByNameAndCountry(parsedSong.artistName(), parsedSong.artistCountry())
                .orElseGet(() -> {
                    Artist newArtist = Artist.builder()
                            .country(parsedSong.artistCountry())
                            .name(parsedSong.artistName())
                            .build();
                    return artistRepository.save(newArtist);
                });

        return Song.builder()
                .title(parsedSong.title())
                .artist(artist)
                .album(parsedSong.album())
                .genres(parsedSong.genres())
                .duration(parsedSong.duration())
                .releaseYear(parsedSong.releaseYear())
                .build();
    }

    private Artist getArtistOrThrow(long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> getResponseStatusExceptionNotFound("Artist with ID ", artistId));
    }

    private Song getSongOrThrow(long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> getResponseStatusExceptionNotFound("Song with ID ", id));
    }

    private void updateSongFromRequest(Song song, SongRequest request) {
        song.setAlbum(request.album());
        song.setTitle(request.title());
        song.setArtist(getArtistOrThrow(request.artistId()));
        song.setGenres(request.genres());
        song.setDuration(request.duration());
        song.setReleaseYear(request.releaseYear());
    }

    private Pageable getSongsPageable(GetSongsRequest request) {
        return PageRequest.of(request.page(), request.size());
    }

    private static ResponseStatusException getResponseStatusExceptionNotFound(String message, long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message + id + " doesn't exist");
    }

    private String formatExcelReportName(Long artistId, String album) {
        if (artistId != null && album != null) {
            return String.format(EXCEL_REPORT_NAME, "artistId_and_album");
        } else if (artistId != null) {
            return String.format(EXCEL_REPORT_NAME, "artistId");
        } else {
            return String.format(EXCEL_REPORT_NAME, "album");
        }
    }
}

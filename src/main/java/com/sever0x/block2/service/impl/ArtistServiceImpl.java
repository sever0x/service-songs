package com.sever0x.block2.service.impl;

import com.sever0x.block2.mapper.ArtistMapper;
import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.repository.ArtistRepository;
import com.sever0x.block2.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistMapper artistMapper;

    private final ArtistRepository artistRepository;

    /**
     * Creates a new artist based on the provided request data.
     *
     * @param request the request containing the artist data
     * @return the response containing the created artist details
     */
    @Override
    public ArtistResponse createArtist(ArtistRequest request) {
        Artist artist = artistMapper.requestToEntity(request);
        if (isArtistExistByName(artist.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Artist with name " + artist.getName() + " already exist");
        }
        return artistMapper.entityToResponse(artistRepository.save(artist));
    }

    /**
     * Retrieves a list of all artists.
     *
     * @return the list of artist responses
     */
    @Override
    public List<ArtistResponse> getArtists() {
        return artistRepository.findAll().stream()
                .map(artistMapper::entityToResponse)
                .toList();
    }

    /**
     * Updates an existing artist with the provided request data.
     *
     * @param id      the ID of the artist to update
     * @param request the request containing the updated artist data
     * @throws ResponseStatusException if the artist with the given ID is not found
     */
    @Override
    public void updateArtistById(long id, ArtistRequest request) {
        Artist updatableArtist = getArtistOrThrow(id);
        updateArtistFromRequest(updatableArtist, request);
        artistRepository.save(updatableArtist);
    }

    /**
     * Deletes an artist by its ID.
     *
     * @param id the ID of the artist to delete
     * @return true if the artist was deleted successfully, false otherwise
     * @throws ResponseStatusException if the artist with the given ID is not found
     */
    @Override
    public boolean deleteArtistById(long id) {
        if (!artistRepository.existsById(id)) {
            throw getResponseStatusExceptionNotFound(id);
        }
        artistRepository.deleteById(id);
        return true;
    }

    private boolean isArtistExistByName(String name) {
        return artistRepository.existsArtistByName(name);
    }

    private Artist getArtistOrThrow(long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> getResponseStatusExceptionNotFound(id));
    }

    private void updateArtistFromRequest(Artist artist, ArtistRequest request) {
        artist.setName(request.name());
        artist.setCountry(request.country());
    }

    private static ResponseStatusException getResponseStatusExceptionNotFound(long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist with ID " + id + " doesn't exist");
    }
}

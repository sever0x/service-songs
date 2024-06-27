package com.sever0x.block2.controller;

import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    /**
     * Creates a new artist based on the provided request data.
     *
     * @param request the request containing the artist data
     * @return the response containing the created artist details
     */
    @PostMapping
    public ArtistResponse createArtist(@RequestBody @Valid ArtistRequest request) {
        return artistService.createArtist(request);
    }

    /**
     * Retrieves a list of all artists.
     *
     * @return the list of artist responses
     */
    @GetMapping
    public List<ArtistResponse> getArtists() {
        return artistService.getArtists();
    }

    /**
     * Updates an existing artist with the provided request data.
     *
     * @param id      the ID of the artist to update
     * @param request the request containing the updated artist data
     */
    @PutMapping("/{id}")
    public void updateArtist(@PathVariable long id, @RequestBody @Valid ArtistRequest request) {
        artistService.updateArtistById(id, request);
    }

    /**
     * Deletes an artist by its ID.
     *
     * @param id the ID of the artist to delete
     * @return true if the artist was deleted successfully, false otherwise
     */
    @DeleteMapping("/{id}")
    public boolean deleteArtist(@PathVariable long id) {
        return artistService.deleteArtistById(id);
    }
}

package com.sever0x.block2.service;

import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;

import java.util.List;

public interface ArtistService {

    ArtistResponse createArtist(ArtistRequest request);

    List<ArtistResponse> getArtists();

    void updateArtistById(long id, ArtistRequest request);

    boolean deleteArtistById(long id);
}

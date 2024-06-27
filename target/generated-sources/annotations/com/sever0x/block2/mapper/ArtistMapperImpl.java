package com.sever0x.block2.mapper;

import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.model.entity.Artist;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-27T12:10:04+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class ArtistMapperImpl implements ArtistMapper {

    @Override
    public Artist requestToEntity(ArtistRequest request) {
        if ( request == null ) {
            return null;
        }

        Artist.ArtistBuilder artist = Artist.builder();

        artist.name( request.name() );
        artist.country( request.country() );

        return artist.build();
    }

    @Override
    public ArtistResponse entityToResponse(Artist artist) {
        if ( artist == null ) {
            return null;
        }

        ArtistResponse.ArtistResponseBuilder artistResponse = ArtistResponse.builder();

        artistResponse.id( artist.getId() );
        artistResponse.name( artist.getName() );
        artistResponse.country( artist.getCountry() );

        return artistResponse.build();
    }
}

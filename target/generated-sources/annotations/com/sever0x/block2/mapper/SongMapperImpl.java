package com.sever0x.block2.mapper;

import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.model.dto.response.ShortSongResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.model.entity.Song;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-27T12:10:04+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class SongMapperImpl extends SongMapper {

    @Override
    public Song requestToEntity(SongRequest request) {
        if ( request == null ) {
            return null;
        }

        Song.SongBuilder song = Song.builder();

        song.title( request.title() );
        song.album( request.album() );
        song.genres( request.genres() );
        song.duration( request.duration() );
        song.releaseYear( request.releaseYear() );

        return song.build();
    }

    @Override
    public SongResponse entityToResponse(Song song) {
        if ( song == null ) {
            return null;
        }

        SongResponse.SongResponseBuilder songResponse = SongResponse.builder();

        songResponse.id( song.getId() );
        songResponse.title( song.getTitle() );
        songResponse.artist( artistToArtistResponse( song.getArtist() ) );
        songResponse.album( song.getAlbum() );
        songResponse.genres( song.getGenres() );
        songResponse.duration( song.getDuration() );
        songResponse.releaseYear( song.getReleaseYear() );

        return songResponse.build();
    }

    @Override
    public ShortSongResponse toShortResponse(Song song) {
        if ( song == null ) {
            return null;
        }

        String artistName = null;
        long id = 0L;
        String title = null;
        String album = null;
        int duration = 0;

        artistName = mapArtistName( song.getArtist() );
        id = song.getId();
        title = song.getTitle();
        album = song.getAlbum();
        duration = song.getDuration();

        ShortSongResponse shortSongResponse = new ShortSongResponse( id, title, artistName, album, duration );

        return shortSongResponse;
    }

    protected ArtistResponse artistToArtistResponse(Artist artist) {
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

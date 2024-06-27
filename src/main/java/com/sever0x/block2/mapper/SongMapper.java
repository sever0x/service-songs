package com.sever0x.block2.mapper;

import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.ShortSongResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.model.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class SongMapper {

    public abstract Song requestToEntity(SongRequest request);

    public abstract SongResponse entityToResponse(Song song);

    @Mapping(target = "artistName", source = "song.artist", qualifiedByName = "mapArtistName")
    public abstract ShortSongResponse toShortResponse(Song song);

    @Named("mapArtistName")
    protected String mapArtistName(Artist artist) {
        return artist.getName();
    }
}

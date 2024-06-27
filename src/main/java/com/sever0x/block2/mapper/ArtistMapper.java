package com.sever0x.block2.mapper;

import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.model.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ArtistMapper {

    Artist requestToEntity(ArtistRequest request);

    ArtistResponse entityToResponse(Artist artist);
}

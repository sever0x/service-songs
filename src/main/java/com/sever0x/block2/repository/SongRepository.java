package com.sever0x.block2.repository;

import com.sever0x.block2.model.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s " +
            "WHERE (:album IS NULL OR s.album = :album) " +
            "AND (:artistId IS NULL OR s.artist.id = :artistId)")
    Page<Song> findAll(@Param("pageable") Pageable pageable,
                       @Param("artistId") Long artistId,
                       @Param("album") String album);
}

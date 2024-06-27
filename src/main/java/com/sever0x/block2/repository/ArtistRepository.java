package com.sever0x.block2.repository;

import com.sever0x.block2.model.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByNameAndCountry(String artistName, String country);

    boolean existsArtistByName(String artistName);
}

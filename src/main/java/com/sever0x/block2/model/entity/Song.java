package com.sever0x.block2.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "songs", indexes = {@Index(name = "idx_album", columnList = "album")})
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    private String album;

    private String genres;

    private int duration;

    private int releaseYear;
}

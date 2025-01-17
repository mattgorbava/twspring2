package com.example.twspring2.database.albums.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode
@Table(name="albums")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "cover")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ImageEntity cover;

    @Column(name = "images")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImageEntity> images;
}

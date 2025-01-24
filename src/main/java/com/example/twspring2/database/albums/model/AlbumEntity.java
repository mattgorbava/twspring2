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

    @Column(name = "title", unique = true)
    private String title;

    @OneToMany(mappedBy="album", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImageEntity> images;

    @Transient
    private ImageEntity coverImage;

    public void setFirstImage(ImageEntity imageEntity) {
        this.coverImage = imageEntity;
    }
}

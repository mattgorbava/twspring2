package com.example.twspring2.database.albums.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String type;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_data", columnDefinition = "BLOB")
    private byte[] imageData;

    @OneToOne(mappedBy = "cover")
    private AlbumEntity album;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "album_id", nullable = false)
    private AlbumEntity albumEntity;
}

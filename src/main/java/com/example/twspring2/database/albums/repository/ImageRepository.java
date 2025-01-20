package com.example.twspring2.database.albums.repository;

import com.example.twspring2.database.albums.model.ImageEntity;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Logger logger = org.slf4j.LoggerFactory.getLogger(ImageRepository.class);

    Optional<ImageEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO images (name, type, image_data, album_id) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void saveImage(String name, String type, byte[] imageData, Long albumId);

    @Query(value = "SELECT id, name, type, image_data, album_id FROM images WHERE album_id = ?1 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    Optional<ImageEntity> findCover(Long albumId);

    Optional<ImageEntity> findFirstByAlbumId(Long albumId);
}

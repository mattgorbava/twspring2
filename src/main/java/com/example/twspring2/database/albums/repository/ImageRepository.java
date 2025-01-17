package com.example.twspring2.database.albums.repository;

import com.example.twspring2.database.albums.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByName(String name);
}

package com.example.twspring2.database.albums.repository;

import com.example.twspring2.database.albums.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumsRepository extends JpaRepository<AlbumEntity, Long> {
}

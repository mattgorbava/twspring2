package com.example.twspring2.database.albums.repository;

import com.example.twspring2.database.albums.model.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumsRepository extends JpaRepository<AlbumEntity, Long> {
    AlbumEntity findByTitle(String title);

    void deleteByTitle(String title);

    @Query("SELECT a FROM AlbumEntity a WHERE a.title ILIKE %?1%")
    List<AlbumEntity> search(String query);
}

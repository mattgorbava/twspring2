package com.example.twspring2.service.albums;

import com.example.twspring2.database.albums.model.AlbumEntity;
import com.example.twspring2.database.albums.model.ImageEntity;
import com.example.twspring2.database.albums.repository.AlbumsRepository;
import com.example.twspring2.database.albums.repository.ImageRepository;
import com.example.twspring2.database.users.model.UserEntity;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    AlbumsRepository albumsRepository;
    ImageRepository imageRepository;
    Logger logger = org.slf4j.LoggerFactory.getLogger(AlbumService.class);

    public AlbumService(AlbumsRepository albumsRepository, ImageRepository imageRepository) {
        this.albumsRepository = albumsRepository;
        this.imageRepository = imageRepository;
    }

    public void save(AlbumEntity album) {
        albumsRepository.save(album);
    }

    public List<AlbumEntity> findAll() {
        List<AlbumEntity> albums = albumsRepository.findAll();
        albums.forEach(album -> {
            Optional<ImageEntity> firstImageOpt = imageRepository.findCover(album.getId());
            firstImageOpt.ifPresent(album::setFirstImage);
        });
        return albums;
    }

    public AlbumEntity findByTitle(String title) {
        return albumsRepository.findByTitle(title);
    }

    public void delete(AlbumEntity album) {
        albumsRepository.delete(album);
    }

    public List<AlbumEntity> search(String query) {
        List<AlbumEntity> albums = albumsRepository.search(query);
        albums.forEach(album -> {
            Optional<ImageEntity> firstImageOpt = imageRepository.findCover(album.getId());
            firstImageOpt.ifPresent(album::setFirstImage);
        });
        return albums;
    }

    public AlbumEntity findById(Long id) {
        return albumsRepository.findById(id).orElse(null);
    }
}

package com.example.twspring2.service.albums;

import com.example.twspring2.database.albums.model.AlbumEntity;
import com.example.twspring2.database.albums.model.ImageEntity;
import com.example.twspring2.database.albums.repository.ImageRepository;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ImageService.class);

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void uploadImage(MultipartFile imageFile, AlbumEntity album) throws IOException {
        ImageEntity imageToSave = new ImageEntity();
        imageToSave.setName(imageFile.getOriginalFilename());
        imageToSave.setType(imageFile.getContentType());
        imageToSave.setImageData(imageFile.getBytes());
        imageToSave.setAlbum(album);
        imageRepository.saveImage(imageToSave.getName(), imageToSave.getType(), imageToSave.getImageData(), imageToSave.getAlbum().getId());
    }

    public byte[] downloadImage(String imageName) {
        Optional<ImageEntity> dbImage = imageRepository.findByName(imageName);

        return dbImage.map(image -> {
            try {
                return ImageUtils.decompressImage(image.getImageData());
            } catch (IOException | DataFormatException e) {
                throw new ContextedRuntimeException("Error downloading image", e)
                        .addContextValue("imageName", imageName)
                        .addContextValue("imageId", image.getId());
            }
        }).orElse(null);
    }
}

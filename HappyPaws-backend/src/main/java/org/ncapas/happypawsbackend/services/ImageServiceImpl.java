package org.ncapas.happypawsbackend.services;

import org.ncapas.happypawsbackend.Domain.Entities.Image;
import org.ncapas.happypawsbackend.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public ImageServiceImpl(CloudinaryService cloudinaryService, ImageRepository imageRepository) {
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
    }

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinaryService.upload(file);

        String imageUrl = (String) uploadResult.get("url");
        String imageId = (String) uploadResult.get("public_id");

        Image newImage = new Image();
        newImage.setName(file.getOriginalFilename());
        newImage.setImgURL(imageUrl);
        newImage.setImageId(imageId);

        return imageRepository.save(newImage);
    }


    @Override
    public void deleteImage(UUID imageId) throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // Eliminar en Cloudinary
        cloudinaryService.delete(image.getName()); // nombre = public_id

        // Eliminar en la base de datos
        imageRepository.delete(image);
    }

}

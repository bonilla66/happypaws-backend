package org.ncapas.happypawsbackend.services;

import org.ncapas.happypawsbackend.Domain.Entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {

    Image uploadImage(MultipartFile file) throws IOException;

    void deleteImage(UUID imageId) throws IOException;

}

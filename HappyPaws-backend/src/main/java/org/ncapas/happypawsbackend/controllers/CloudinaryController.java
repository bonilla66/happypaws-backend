package org.ncapas.happypawsbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.ncapas.happypawsbackend.Domain.Entities.Image;
import org.ncapas.happypawsbackend.Domain.dtos.ImageDTO;
import org.ncapas.happypawsbackend.services.CloudinaryService;
import org.ncapas.happypawsbackend.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class CloudinaryController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageDTO> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Image image = imageService.uploadImage(file);
            ImageDTO dto = new ImageDTO(image.getId(), image.getName(), image.getImgURL());
            return ResponseEntity.ok(dto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable UUID id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo eliminar la imagen: " + e.getMessage());
        }
    }


}



package com.info_hub.controllers;

import com.info_hub.models.Image;
import com.info_hub.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class ImageController {
    private final ImageService imageService; // upload image.

    @GetMapping("/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("storages/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("storages/default.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Upload image.
     *
     * @param img < 5MB
     * @return url of image.
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<Image> uploadImages(@RequestParam("file") MultipartFile img) {
        Image image = imageService.createImage(img);
        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

}

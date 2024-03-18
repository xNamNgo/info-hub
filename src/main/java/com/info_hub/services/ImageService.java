package com.info_hub.services;

import com.info_hub.components.ImageHandler;
import com.info_hub.models.Image;
import com.info_hub.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageHandler imageHandler;
    private final ImageRepository imageRepository;

    // Tạm thời xử lí 1 folder chứa all ảnh
    public Image createImage(MultipartFile imageFile) {
        Image image = null;
        try {
            image = imageHandler.saveImage(imageFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image.", e);
        }

        return imageRepository.save(image);
    }
}

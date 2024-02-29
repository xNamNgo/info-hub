package com.info_hub.services;

import com.info_hub.components.ImageHandler;
import com.info_hub.models.Image;
import com.info_hub.repositories.ImageRepository;
import com.info_hub.responses.article.ImgResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageHandler imageHandler;
    private final ImageRepository imageRepository;

    public ImgResponse createImageArticle(MultipartFile imageFile) {
        String imagePath = null;
        try {
            imagePath = imageHandler.saveArticleImage(imageFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save article image.", e);
        }

        Image image = new Image(imagePath);
        imageRepository.save(image);
        return new ImgResponse(image.getUrl());
    }

    // avatar

}

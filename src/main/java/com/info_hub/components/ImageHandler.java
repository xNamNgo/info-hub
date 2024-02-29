package com.info_hub.components;

import com.info_hub.constant.EnvironmentConstant;
import com.info_hub.exceptions.ImgSizeException;
import com.info_hub.exceptions.MediaTypeException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class ImageHandler {

    public String saveArticleImage(MultipartFile imageFile) throws IOException {
        validateImageInput(imageFile);
        return storeImageInArticleFolder(imageFile);
    }

    private void validateImageInput(MultipartFile imageFile) {
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new ImgSizeException("File is too large! Maximum size is 5MB");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new MediaTypeException("File must be an image");
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeImageInArticleFolder(MultipartFile imageFile) throws IOException {
        if (!isImageFile(imageFile) || imageFile.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uniqueFileName = generateUniqueFileName(originalFileName);

        Path uploadDirectory = Paths.get(EnvironmentConstant.UPLOADS_FOLDER, "/article");

        createDirectoryIfNotExists(uploadDirectory);

        Path destination = Paths.get(uploadDirectory.toString(), uniqueFileName);

        Files.copy(imageFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    private String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    private void createDirectoryIfNotExists(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }
}

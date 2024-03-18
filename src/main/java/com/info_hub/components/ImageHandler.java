package com.info_hub.components;

import com.info_hub.constant.EnvironmentConstant;
import com.info_hub.exceptions.ImgSizeException;
import com.info_hub.exceptions.MediaTypeException;
import com.info_hub.models.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Component
public class ImageHandler {
    @Value("${api.url-image}")
    private String ipImage;

    public Image saveImage(MultipartFile imageFile) throws IOException {
        validateImageInput(imageFile);
        return storeImageFolder(imageFile);
    }

    private void validateImageInput(MultipartFile imageFile) {
        if(imageFile == null) {
            throw new ImgSizeException("File cannot empty");
        }

        if (imageFile.getSize() > 300 * 1024 * 1024) {
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

    private Image storeImageFolder(MultipartFile imageFile) throws IOException {
        if (!isImageFile(imageFile) || imageFile.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uniqueFileName = generateUniqueFileName(originalFileName);

        // Tạm thời xử lí 1 folder chứa all ảnh
        // Đường dẫn chứa ảnh.
        Path uploadDirectory = Paths.get(EnvironmentConstant.UPLOADS_FOLDER);

        createDirectoryIfNotExists(uploadDirectory);

        Path destination = Paths.get(uploadDirectory.toString(), uniqueFileName);

        // Coppy file ảnh vào local
        Files.copy(imageFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // api get image.
        final String imageUrl = ipImage + uniqueFileName;

        return Image.builder()
                .name(uniqueFileName)
                .size(imageFile.getSize())
                .mime(imageFile.getContentType())
                .url(imageUrl)
                .build();
    }


    private String generateUniqueFileName(String originalFileName) {
        // Và fix trường hợp Screen shot -> Screen_shot
        String timestamp = String.valueOf(System.currentTimeMillis());
        return  timestamp + originalFileName.replace(" ","_");
    }

    private void createDirectoryIfNotExists(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }
}

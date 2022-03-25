package com.whatsapp.image_service.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.models.ImageMetadata;
import com.whatsapp.image_service.repositories.ImageMetadataRepo;
import com.whatsapp.image_service.repositories.ImageRepo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    @Value("${image.upload.dir}")
    private String imagesDirectory;
    private final ImageRepo imageRepo;
    private final ImageMetadataRepo imageMetadataRepo;
    private final ImageProcessor imageProcessor;

    public boolean storeImage(MultipartFile file, String userId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileLocation = imagesDirectory + userId;
        Path uploadPath = Paths.get(fileLocation);
        try (InputStream inputStream = file.getInputStream()) {
            saveImageToFileSystem(fileName, uploadPath, inputStream);
        } catch (IOException ioe) {
            log.error("Could not save image file: " + fileName + " for userId: " + userId, ioe);
            return false;
        }
        saveImageInDb(file, userId, fileName, fileLocation);
        imageProcessor.storeImageInDifferentFormats(file, userId);
        return true;
    }

    private void saveImageInDb(MultipartFile file, String userId, String fileName, String fileLocation) {
        Image image = new Image();
        image.setUserId(userId);
        image.setImgName(fileName);
        ImageMetadata imageMetadata = ImageMetadata.builder()
                .imgType(file.getContentType())
                .createdAt(LocalDate.now())
                .imgUrl(fileLocation)
                .build();
        image.setMetadata(List.of(imageMetadata));
        imageMetadataRepo.save(imageMetadata);
        imageRepo.save(image);
    }

    private void saveImageToFileSystem(String fileName, Path uploadPath, InputStream inputStream) throws IOException {
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }
}

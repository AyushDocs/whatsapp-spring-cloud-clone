package com.whatsapp.image_service.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import javax.imageio.ImageIO;

import com.whatsapp.image_service.configuration.ImageConfig;
import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.models.ImageMetadata;
import com.whatsapp.image_service.repositories.ImageMetadataRepo;
import com.whatsapp.image_service.repositories.ImageRepo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Async
@Slf4j
@RequiredArgsConstructor
public class ImageProcessor {
    private final ImageConfig imageConfig;
    private final ImageRepo imageRepo;
    private final ImageMetadataRepo imageMetadataRepo;

    public void storeImageInDifferentFormats(MultipartFile file, String userId) {
        try {
            String fileType = file.getContentType();
            if (fileType.equals("image/jpeg"))
                saveFile(file, userId, "png");
            else
                saveFile(file, userId, "jpeg");
        } catch (IOException e) {
            log.error("Could not save image file: " + file.getOriginalFilename() + " for userId: " + userId, e);
        }
    }

    private void saveFile(MultipartFile multiPartFile, String userId, String fileType)
            throws IOException {
        String fileName=StringUtils.cleanPath(multiPartFile.getOriginalFilename());    
        createBlankImageInDir(multiPartFile,fileName, userId);
        saveFileInDir(multiPartFile, fileName, fileType,userId);
        saveImageDataInDb(userId, fileName, fileType);
    }

    private void saveFileInDir(MultipartFile multiPartFile, String fileName, String fileType,String userId) throws IOException {
        InputStream is = multiPartFile.getInputStream();
        BufferedImage img = ImageIO.read(is);
        String fullPathToFile = "%s%s/%s".formatted(imageConfig.getImageUploadDir(), userId, fileName);
        File file = Paths.get(fullPathToFile).toFile();
        ImageIO.write(img, fileType, file);
    }

    private void createBlankImageInDir(MultipartFile multiPartFile,String fileName, String userId) throws IOException {
        Path uploadPath = getUserImagesDirPath(userId);// user-images/5
        Path filePath = uploadPath.resolve(fileName);// user-images/5/a.jpg
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);
        Files.copy(multiPartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private Path getUserImagesDirPath(String userId) {
        String userImagesDirPath = imageConfig.getImageUploadDir() + userId;
        return Paths.get(userImagesDirPath);
    }

    private void saveImageDataInDb(String userId, String fileName, String fileType) {
        String fileLocation ="/api/v1/images/%s/%s".formatted(userId,fileName);
        ImageMetadata imageMetadata = createImageMetadataForImage(fileType, fileLocation);
        Image image = createImage(userId, fileName, fileType, imageMetadata);
        imageMetadataRepo.save(imageMetadata);
        imageRepo.save(image);

    }

    private Image createImage(String userId, String fileName, String fileType, ImageMetadata imageMetadata) {
        return Image.builder()
                .userId(userId)
                .imgName(fileName + "." + fileType)
                .metadata(List.of(imageMetadata))
                .build();
    }

    private ImageMetadata createImageMetadataForImage(String fileType, String fileLocation) {
       return ImageMetadata.builder()
                .createdAt(LocalDate.now())
                .imgUrl(fileLocation + "."+ fileType)
                .imgType(fileType)
                .build();
    }
}

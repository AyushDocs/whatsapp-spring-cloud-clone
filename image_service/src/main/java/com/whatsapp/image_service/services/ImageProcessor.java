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

import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.models.ImageMetadata;
import com.whatsapp.image_service.repositories.ImageMetadataRepo;
import com.whatsapp.image_service.repositories.ImageRepo;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${image.upload.dir}")
    private String imagesDirectory;
    private final ImageRepo imageRepo;
    private final ImageMetadataRepo imageMetadataRepo;

    public void storeImageInDifferentFormats(MultipartFile file, String userId) {
        try {
            if(file.getContentType().equals("image/jpeg")) saveFile(file, userId, "png");
            else saveFile(file, userId, "jpeg");
        } catch (IOException e) {
            log.error("Could not save image file: " + file.getOriginalFilename() + " for userId: " + userId, e);
        }
    }

    private void saveFile(MultipartFile multiPartFile, String userId, String fileType)
            throws IOException {
        String fileLocation = imagesDirectory + userId;
        String fileName = StringUtils.cleanPath(multiPartFile.getOriginalFilename());
        Path uploadPath = Paths.get(fileLocation);
        Path filePath = uploadPath.resolve(fileName);
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);
        Files.copy(multiPartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        InputStream is = multiPartFile.getInputStream();
        BufferedImage img = ImageIO.read(is);
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        File file = Paths.get(imagesDirectory + userId + "/" + fileName + "." + fileType)
                .toFile();
        ImageIO.write(img, fileType, file);
        saveImageInDb(userId, fileName, fileType);
    }

    private void saveImageInDb(String userId, String fileName,String fileType) {
        String fileLocation = "/api/v1/users/" + userId + "/images/" + fileName;
        ImageMetadata imageMetadata = ImageMetadata.builder()
                .imgType("image/"+fileType)
                .createdAt(LocalDate.now())
                .imgUrl(fileLocation + "."+ fileType)
                .build();
        Image image = Image.builder()
                .userId(userId)
                .imgName(fileName+"."+ fileType)
                .metadata(List.of(imageMetadata))
                .build();
        imageMetadataRepo.save(imageMetadata);
        imageRepo.save(image);

    }
}

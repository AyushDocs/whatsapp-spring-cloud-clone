package com.whatsapp.image_service.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Async
@Slf4j
public class ImageProcessor {
    @Value("${image.upload.dir}")
    private String imagesDirectory;

    public void storeImageInDifferentFormats(MultipartFile file, String userId) {
        try {
            saveFile(file, userId, "png");
            saveFile(file, userId, "jpeg");
        } catch (IOException e) {
            log.error("Could not save image file: " + file.getOriginalFilename() + " for userId: " + userId, e);
        }
    }

    private void saveFile(MultipartFile multiPartfile, String userId, String fileType)
            throws IOException {
        String fileLocation = imagesDirectory + userId;
        String fileName = StringUtils.cleanPath(multiPartfile.getOriginalFilename());
        Path uploadPath = Paths.get(fileLocation);
        Path filePath = uploadPath.resolve(fileName);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        Files.copy(multiPartfile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        InputStream is = multiPartfile.getInputStream();
        BufferedImage img = ImageIO.read(is);
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        File file = Paths.get(imagesDirectory + userId + "/" + fileName + "." + fileType)
                .toFile();
        ImageIO.write(img, fileType, file);
    }
}

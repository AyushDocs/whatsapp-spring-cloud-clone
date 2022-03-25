package com.whatsapp.image_service.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

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
        if (file.getContentType().equals("image/jpeg"))
            saveFile(file, userId, "png");
        else if (file.getContentType().equals("image/png"))
            saveFile(file, userId, "jpeg");
    }

    private void saveFile(MultipartFile file, String userId, String fileType) {
        try (InputStream is = file.getInputStream()) {
            BufferedImage img = ImageIO.read(is);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileName=fileName.substring(0, fileName.lastIndexOf("."));
            File filePath = Paths.get(imagesDirectory + userId + "/" + fileName + "." + fileType)
                    .toFile();
            ImageIO.write(img, fileType, filePath);
        } catch (Exception e) {
            log.error("Could not save image file: " + file.getOriginalFilename() + " for userId: " + userId + "type: "
                    + fileType, e);
        }
    }
}

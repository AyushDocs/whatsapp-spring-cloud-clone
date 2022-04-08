package com.whatsapp.image_service.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.whatsapp.image_service.configuration.ImageConfig;
import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.repositories.ImageRepo;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageConfig imageConfig;
    public void storeImage(MultipartFile file, String userId) {
        storeImageInDifferentFormats(file, userId);
        saveImageInDb(file, userId);
    }
    public String findImages(String imageUrl,String userId){
        
        try {
            String imageFilePath ="%s/%s/%s".formatted(
                    imageConfig.getImageUploadDir(),
                    userId,
                    imageUrl);
            File imageFile = new File(imageFilePath);
            byte[] fileContent = FileUtils.readFileToByteArray(imageFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            String fileType = imageUrl.substring(imageUrl.lastIndexOf("."));
            return "data:image/%s;base64,%s".formatted(fileType, encodedString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveImageInDb(MultipartFile file, String userId) {
        String fileName = getFileName(file);
        Image image =new Image(null,userId,fileName);
        imageRepo.save(image);
    }

    private String getFileName(MultipartFile file) {
        return StringUtils.cleanPath(file.getOriginalFilename());
    }
    public void storeImageInDifferentFormats(MultipartFile file, String userId) {
        try { 
            for(String format:imageConfig.getImageFormats()){
                saveFile(file, userId, format);
            }
        } catch (IOException e) {
            log.error("Could not save image file: " + file.getOriginalFilename() + " for userId: " + userId, e);
        }
    }

    private void saveFile(MultipartFile multiPartFile, String userId, String fileType)
            throws IOException {
        String fileName = getFileName(multiPartFile, fileType);
        createBlankImageInDir(multiPartFile, fileName, userId);
        saveFileInDir(multiPartFile, fileName, fileType, userId);
    }
    private String getFileName(MultipartFile file, String fileType) {
        String fileName = getFileName(file);
        fileName=fileName.substring(0,fileName.lastIndexOf("."))+ "." + fileType;
        return fileName;
    }

    private void saveFileInDir(MultipartFile multiPartFile, String fileName, String fileType, String userId)
            throws IOException {
        InputStream is = multiPartFile.getInputStream();
        BufferedImage img = ImageIO.read(is);
        String fullPathToFile = "%s/%s/%s".formatted(imageConfig.getImageUploadDir(), userId, fileName);
        File file = Paths.get(fullPathToFile).toFile();
        ImageIO.write(img, fileType, file);
    }

    private void createBlankImageInDir(MultipartFile multiPartFile, String fileName, String userId) throws IOException {
        Path uploadPath = getUserImagesDirPath(userId);// user-images/5
        Path filePath = uploadPath.resolve(fileName);// user-images/5/a.jpg
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);
        Files.copy(multiPartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private Path getUserImagesDirPath(String userId) {
        String userImagesDirPath = imageConfig.getImageUploadDir()+"/" + userId;
        return Paths.get(userImagesDirPath);
    }
}

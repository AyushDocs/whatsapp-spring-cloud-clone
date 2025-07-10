package com.whatsapp.image_service.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import com.whatsapp.image_service.configuration.ImageConfig;
import com.whatsapp.image_service.configuration.ImageTypes;
import com.whatsapp.image_service.models.ImageMetadata;
import com.whatsapp.image_service.models.UserImage;
import com.whatsapp.image_service.repositories.ImageRepo;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageConfig imageConfig;
    private static final String IMAGE_STORAGE_ERROR_MESSAGE_FORMAT = "Error while storing profile image for userId {}";

    public void storeProfileImage(MultipartFile file, String userId) {
        ImageStorage storage = new ImageStorage(file, userId, ImageTypes.PROFILE);
        try {
            storage.storeImage();
        } catch (IOException e) {
            log.error(IMAGE_STORAGE_ERROR_MESSAGE_FORMAT,userId, e);
        }
        saveImageInDb(userId, ImageTypes.PROFILE);
    }

    public String findImages(String imageUrl, String userId) {
        String imageFilePath = "%s/%s".formatted(getUserImagesDirPath(userId),imageUrl);
        File imageFile = new File(imageFilePath);

        try {
            byte[] fileContent = FileUtils.readFileToByteArray(imageFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            String fileType = imageUrl.substring(imageUrl.lastIndexOf("."));
            return "data:image/%s;base64,%s".formatted(fileType, encodedString);
        } catch (IOException e) {
           log.error("could not find image with imageUrl {} and userId {}", imageUrl,userId,e);
           return null;
        }
    }

    private void saveImageInDb(String userId,ImageTypes imageType) {
        ImageMetadata imageMetadata = new ImageMetadata(null,imageType);
        UserImage image = new UserImage(null, userId, List.of(imageMetadata));
        imageRepo.save(image);
    }

    private Path getUserImagesDirPath(String userId) {
        String userImagesDirPath = "%s/%s"
                .formatted(imageConfig.getImageUploadDir(), userId);
        return Paths.get(userImagesDirPath);
    }
}

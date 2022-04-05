package com.whatsapp.image_service.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import com.whatsapp.image_service.configuration.ImageConfig;
import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.models.ImageMetadata;
import com.whatsapp.image_service.repositories.ImageMetadataRepo;
import com.whatsapp.image_service.repositories.ImageRepo;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageMetadataRepo imageMetadataRepo;
    private final ImageProcessor imageProcessor;
    private final ImageConfig imageConfig;

    public Image storeImage(MultipartFile file, String userId) {
        imageProcessor.storeImageInDifferentFormats(file, userId);
        return saveImageInDb(file, userId);
    }
    public String findImages(String imageUrl,String userId){
        
        try {
            String imageFilePath ="%s%s/%s".formatted(
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

    private Image saveImageInDb(MultipartFile file, String userId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileLocation = "/api/v1/users/" + userId + "/images/" + fileName;
        ImageMetadata imageMetadata = ImageMetadata.builder()
                .imgType(file.getContentType())
                .createdAt(LocalDate.now())
                .imgUrl(fileLocation)
                .build();
        Image image = Image.builder()
                .userId(userId)
                .imgName(fileName)
                .metadata(List.of(imageMetadata))
                .build();
        imageMetadataRepo.save(imageMetadata);
        return imageRepo.save(image);
    }
}

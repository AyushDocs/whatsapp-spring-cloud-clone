package com.whatsapp.image_service.services;

import java.time.LocalDate;
import java.util.List;

import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.models.ImageMetadata;
import com.whatsapp.image_service.repositories.ImageMetadataRepo;
import com.whatsapp.image_service.repositories.ImageRepo;

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

    public boolean storeImage(MultipartFile file, String userId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        saveImageInDb(file, userId, fileName);
        imageProcessor.storeImageInDifferentFormats(file, userId);
        return true;
    }

    private void saveImageInDb(MultipartFile file, String userId, String fileName) {
        String fileLocation ="/api/v1/users/"+ userId+"/images/"+fileName;
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
}

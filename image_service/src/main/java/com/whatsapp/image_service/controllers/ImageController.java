package com.whatsapp.image_service.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.whatsapp.image_service.models.UserImage;
import com.whatsapp.image_service.services.ImageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/images/{userId}")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadImage(@RequestParam MultipartFile image,@PathVariable String userId) {
       imageService.storeProfileImage(image, userId);
    }

    @GetMapping("/{imageUrl}")
    public ResponseEntity<?> findImage(@PathVariable String userId,@PathVariable String imageUrl){
        String imageInFileSystemPath = imageService.findImages(imageUrl, userId);
        if(imageInFileSystemPath == null) return ResponseEntity.badRequest().build();
        Map<String,String> map =Map.of("imageUrl", imageInFileSystemPath);
        return ResponseEntity.ok(map);
    }
}

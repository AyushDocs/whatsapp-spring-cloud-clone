package com.whatsapp.image_service.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;

import com.whatsapp.image_service.models.Image;
import com.whatsapp.image_service.services.ImageService;

import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1//images/{userId}")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
            @PathVariable String userId) throws URISyntaxException {
        Image image = imageService.storeImage(file, userId);
        String path = String.format("/api/v1/users/%s/images/%s", userId, image.getId());
        return ResponseEntity.created(new URI(path)).body(image);
    }

    @GetMapping("/{imageUrl}")
    public ResponseEntity<?> findImage(@PathVariable String userId,@PathVariable String imageUrl){
        String imageInFileSystemPath = imageService.findImages(imageUrl, userId);
        if(imageInFileSystemPath == null) return ResponseEntity.badRequest().build();
        Map<String,String> map =Map.of("imageUrl", imageInFileSystemPath);
        return ResponseEntity.ok(map);
    }
}

package com.whatsapp.image_service.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import com.whatsapp.image_service.services.ImageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @PostMapping("/users/{userId}/images")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
                                @PathVariable String userId) throws URISyntaxException {
        boolean storedImageSuccessfully = imageService.storeImage(file,userId);
        if(!storedImageSuccessfully) return ResponseEntity.internalServerError().build();
        return ResponseEntity.created(new URI("/api/v1/users/5/images/imageId")).build();
    }
}

package com.whatsapp.image_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@Data
public class ImageConfig {
   @Value("${image.upload.dir}")
   private String imageUploadDir;
}
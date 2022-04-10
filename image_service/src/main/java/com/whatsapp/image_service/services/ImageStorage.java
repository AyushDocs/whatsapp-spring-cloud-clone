package com.whatsapp.image_service.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.whatsapp.image_service.configuration.ImageConfig;
import com.whatsapp.image_service.configuration.ImageTypes;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ImageStorage {
      private final MultipartFile multiPartFile;
      private final String userId;
      private final ImageTypes imageType;
      private ImageConfig imageConfig;

      public void storeImage() throws IOException {
            createBlankDir(userId);
            for(String format: imageConfig.getImageFormats())
                      saveFileAs(format);
      }

      private void createBlankDir(String userId) throws IOException {
            Path uploadPath = getUserImagesDirPath(userId);// user-images/5
            if (!Files.exists(uploadPath))  Files.createDirectories(uploadPath);
      }
      private void saveFileAs(String format) throws IOException {
            BufferedImage img = getBufferedImage();
            String fullPathToFile = "%s/%s.%s".formatted(getUserImagesDirPath(userId), imageType.toString(), format);
            File file = Paths.get(fullPathToFile).toFile();
            ImageIO.write(img, format, file);
      }

      private BufferedImage getBufferedImage() throws IOException {
            InputStream is = multiPartFile.getInputStream();
            return ImageIO.read(is);
      }
      
      private Path getUserImagesDirPath(String userId) {
            String userImagesDirPath ="%s/%s"
            .formatted(imageConfig.getImageUploadDir(),userId);
            return Paths.get(userImagesDirPath);
      }
}

package com.whatsapp.image_service.repositories;

import com.whatsapp.image_service.models.ImageMetadata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetadataRepo extends JpaRepository<ImageMetadata, Long> {

}

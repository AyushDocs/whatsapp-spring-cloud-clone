package com.whatsapp.image_service.repositories;

import com.whatsapp.image_service.models.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {

}

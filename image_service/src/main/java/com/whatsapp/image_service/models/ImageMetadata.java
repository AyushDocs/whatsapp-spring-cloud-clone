package com.whatsapp.image_service.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
@Entity
@Data
@Table(name = "image_metadata_tbl")

@Builder
public class ImageMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgUrl;
    private String imgType;
    private LocalDate createdAt;
}

package com.whatsapp.image_service.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "image_tbl")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String imgName;
    @OneToMany
    private List<ImageMetadata> metadata;
}

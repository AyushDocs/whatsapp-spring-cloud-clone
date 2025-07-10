package com.whatsapp.image_service.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.whatsapp.image_service.configuration.ImageTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "image_tbl")
@AllArgsConstructor
@NoArgsConstructor
public class ImageMetadata {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      @Enumerated(EnumType.STRING)
      private ImageTypes imgType;
}

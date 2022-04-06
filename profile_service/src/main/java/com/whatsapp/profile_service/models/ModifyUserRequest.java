package com.whatsapp.profile_service.models;

import javax.annotation.Nonnull;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserRequest {
      @Nonnull
      private String name;
      @Email
      private String email;
      @URL
      private String imageUrl;
}

package com.whatsapp.profile_service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
      @NotNull
      @NotBlank
      private String name;
      @Email
      @NotNull
      @NotBlank
      private String email;
      @URL
      @NotNull
      @NotBlank
      private String imageUrl;
}

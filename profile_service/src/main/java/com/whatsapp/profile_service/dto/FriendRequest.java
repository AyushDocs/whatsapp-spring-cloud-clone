package com.whatsapp.profile_service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "builder")
public class FriendRequest {
      @NotBlank
      @NotNull
      private String textInput;
      private int page;
      private int offset;
}

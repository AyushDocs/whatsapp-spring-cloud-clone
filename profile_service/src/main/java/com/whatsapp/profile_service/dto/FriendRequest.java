package com.whatsapp.profile_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "builder")
public class FriendRequest {
      private String textInput;
      private int page;
      private int offset;
}

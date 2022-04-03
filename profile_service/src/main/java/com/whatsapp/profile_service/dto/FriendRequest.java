package com.whatsapp.profile_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequest {
      private String textInput;
      private int page;
      private int offset;
}

package com.whatsapp.message_service.dto;

import java.time.LocalDateTime;

import com.whatsapp.message_service.models.Message;
import com.whatsapp.message_service.models.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String content;
    private String sentBy;
    private String sentTo;
    public Message toMessage(){
        return Message.builder()
                .content(content)
                .sentBy(sentBy)
                .sentTo(sentTo)
                .timestamp(LocalDateTime.now())
                .status(Status.RECEIVED_BY_SERVER)
                .build();
    }
}
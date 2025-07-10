package com.whatsapp.message_service.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.whatsapp.message_service.models.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    @NotBlank
    @NotNull
    @NotEmpty
    private String content;
    @NotBlank
    @NotNull
    @NotEmpty
    @Email
    private String sentBy;
    public Message toMessage(){
        return Message.builder()
                .content(content)
                .sentBy(sentBy)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
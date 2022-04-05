package com.whatsapp.message_service.controllers;

import java.util.List;

import com.whatsapp.message_service.dto.MessageDto;
import com.whatsapp.message_service.models.Message;
import com.whatsapp.message_service.services.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequestMapping("/api/v1/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@RequestBody MessageDto messageDto){
        messageService.saveMessage(messageDto.toMessage());
    }
    @GetMapping("/{forWhom}")
    public ResponseEntity<List<Message>> findMessages(@PathVariable String forWhom){
        List<Message> messages = messageService.findMessages(forWhom);
        if(messages.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(messages);
    }
    @PutMapping("/{id}")
    public void updateMessageState(@PathVariable Long id){
       messageService.updateMessageState(id);
    }
}

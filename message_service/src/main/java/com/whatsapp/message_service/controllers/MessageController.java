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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/messages")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    public void saveMessage(@RequestBody MessageDto messageDto){
        String content = messageDto.getContent();
        String sentBy = messageDto.getSentBy();
        String sentTo=messageDto.getSentTo();
        messageService.saveMessage(content, sentBy,sentTo);
    }
    @GetMapping("/{forWhom}")
    public ResponseEntity<List<Message>> findMessages(@PathVariable String forWhom){
        List<Message> messages = messageService.findMessages(forWhom);
        if(messages.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(messages);
    }
    @PutMapping("/{id}")
    public ResponseEntity<List<Message>> updateMessageState(@PathVariable Long id){
        boolean messageExists = messageService.updateMessageStateToSentToFriend(id);
        if(!messageExists) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
}

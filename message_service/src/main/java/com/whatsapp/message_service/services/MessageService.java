package com.whatsapp.message_service.services;

import com.whatsapp.message_service.models.Message;
import com.whatsapp.message_service.repositories.MessageRepo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepo messageRepo;

    public void saveMessage(Message message) {
        messageRepo.save(message);
    }
}
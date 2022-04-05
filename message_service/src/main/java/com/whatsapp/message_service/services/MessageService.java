package com.whatsapp.message_service.services;

import java.util.List;
import java.util.Optional;

import com.whatsapp.message_service.exceptions.MessageNotFoundException;
import com.whatsapp.message_service.models.Message;
import com.whatsapp.message_service.models.Status;
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

    public List<Message> findMessages(String forWhom) {
        return messageRepo.findUreadMessages(forWhom);
    }

    public void updateMessageState(Long id) {
        Message m = getMessageFromDb(id);
        if (m.getStatus().equals(Status.SENT_TO_FRIEND)) m.setStatus(Status.SENT_TO_FRIEND);
        else m.setStatus(Status.RECEIVED_BY_FRIEND);
        messageRepo.save(m);
    }

    private Message getMessageFromDb(Long id) {
        Optional<Message> message = messageRepo.findById(id);
        return message.orElseThrow(MessageNotFoundException::new);
    }
}
package com.whatsapp.message_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.whatsapp.message_service.models.Message;
import com.whatsapp.message_service.models.Status;
import com.whatsapp.message_service.repositories.MessageRepo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepo messageRepo;

    public void saveMessage(String content, String sentBy, String sentTo) {
        Message message = Message.builder()
                .content(content)
                .sentBy(sentBy)
                .sentTo(sentTo)
                .timestamp(LocalDateTime.now())
                .status(Status.RECEIVED_BY_SERVER)
                .build();
        messageRepo.save(message);
    }

    public List<Message> findMessages(String forWhom) {
        return messageRepo.findUreadMessages(forWhom);
    }

    public boolean updateMessageStateToSentToFriend(Long id) {
        Optional<Message> message = messageRepo.findById(id);
        if (!message.isPresent()) return message.isPresent();
        Message m = message.get();
        if (m.getStatus().equals(Status.SENT_TO_FRIEND)) m.setStatus(Status.SENT_TO_FRIEND);
        else m.setStatus(Status.RECEIVED_BY_FRIEND);
        messageRepo.save(m);
        return message.isPresent();
    }
}
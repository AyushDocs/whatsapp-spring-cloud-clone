package com.whatsapp.message_service.repositories;

import java.util.List;

import com.whatsapp.message_service.models.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m" +
            " WHERE m.sentTo=?1" +
            " AND (m.status=com.whatsapp.message_service.models.Status.RECEIVED_BY_SERVER"+
            " or m.status=com.whatsapp.message_service.models.Status.SENT_TO_FRIEND)")
    List<Message> findUreadMessages(String forWhom);
    // Optional<List<Message>> findMessagesBySentToAndStatus(String forWhom,List<Status> statues);
}

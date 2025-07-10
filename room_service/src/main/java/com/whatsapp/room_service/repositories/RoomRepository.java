package com.whatsapp.room_service.repositories;

import com.whatsapp.room_service.models.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
      boolean existsByUserEmails(String userEmails);
}
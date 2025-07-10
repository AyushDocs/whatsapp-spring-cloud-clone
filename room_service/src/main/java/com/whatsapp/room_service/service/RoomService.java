package com.whatsapp.room_service.service;

import java.util.List;

import com.whatsapp.room_service.dto.RoomDto;
import com.whatsapp.room_service.exceptions.RoomWithSameNameAlreadyExistsException;
import com.whatsapp.room_service.exceptions.UsersAlreadyInARoomException;
import com.whatsapp.room_service.models.Room;
import com.whatsapp.room_service.repositories.RoomRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
      private final RoomRepository repository;

      public Room createRoom(RoomDto dto) {
            String userEmails = dto.getUserEmailsAsString();
            if (repository.existsByUserEmails(userEmails)) throw new UsersAlreadyInARoomException();
           return repository.save(dto.toRoom());
      }

}
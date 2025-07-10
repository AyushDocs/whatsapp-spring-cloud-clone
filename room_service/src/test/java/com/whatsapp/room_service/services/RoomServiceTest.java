package com.whatsapp.room_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.whatsapp.room_service.dto.RoomDto;
import com.whatsapp.room_service.exceptions.UsersAlreadyInARoomException;
import com.whatsapp.room_service.models.Room;
import com.whatsapp.room_service.repositories.RoomRepository;
import com.whatsapp.room_service.service.RoomService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
      private static final Room ROOM = new Room(1l, "uuid", "name", "userEmails");
      private static final RoomDto ROOM_DTO = new RoomDto(List.of("a@g.com", "b@g.com"), "name");
      private RoomService underTest;
      @Mock
      private RoomRepository repository;

      @BeforeEach
      void init() {
            underTest = new RoomService(repository);
      }

      @Test
      void should_not_save_room_user_group_already_exists() {
            when(repository.existsByUserEmails(anyString())).thenReturn(true);
            RoomDto dto = new RoomDto(List.of("a", "b"), "name");
            assertThrows(UsersAlreadyInARoomException.class, () -> underTest.createRoom(dto));
            verify(repository).existsByUserEmails(anyString());
      }

      @Test
      void should_save_room() {
            // arrange
            when(repository.existsByUserEmails(anyString())).thenReturn(false);
            when(repository.save(any(Room.class))).thenReturn(ROOM);
            ArgumentCaptor<Room> ac = ArgumentCaptor.forClass(Room.class);
            // assert
            Room savedRoom = underTest.createRoom(ROOM_DTO);
            // then
            verify(repository).existsByUserEmails(anyString());
            verify(repository).save(ac.capture());
            Room room = ac.getValue();
            assertEquals("name", room.getName());
            assertEquals(2,ROOM_DTO.getUserEmails().size());
            assertEquals(ROOM, savedRoom);
      }
      @Test
      void should_get__rooms() {
            
      }
}
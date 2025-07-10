package com.whatsapp.room_service.controller;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.room_service.controllers.RoomController;
import com.whatsapp.room_service.dto.RoomDto;
import com.whatsapp.room_service.service.RoomService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
@WebMvcTest(RoomController.class)
@RunWith(SpringRunner.class)
class RoomControllerTest {
      @Autowired
      private MockMvc mvc;
      private final ObjectMapper mapper = new ObjectMapper();
      @MockBean private RoomService roomService;

      @Test
      void create_room_should_fail_invalid_content_type() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/rooms/");
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void create_room_should_fail_no_body() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/rooms/")
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      // TODO: later
      @Test
      void should_not_create_room_invalid_number_of_users_provided() throws Exception {
            RoomDto dto = new RoomDto(List.of("a@g.com"), "name");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/rooms/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }
      // TODO: later

      @ParameterizedTest
      @NullAndEmptySource
      @CsvSource({ " " })
      void should_not_create_room_invalid_email(String email) throws Exception {
            List<String> l=new ArrayList<>() {
                  {
                        add(email);
                        add("a@g.com");
                  }
            };
            RoomDto dto = new RoomDto(l,"name");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/rooms/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }
      
      @Test
      void should_create_room() throws Exception {
            RoomDto dto = new RoomDto(List.of("a@g.com","b@g.com"), "name");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/rooms/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isCreated());
            verify(roomService).createRoom(dto);
      }

      private String asJsonString(Object obj) throws JsonProcessingException {
            return mapper.writeValueAsString(obj);
      }
}

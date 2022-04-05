package com.whatsapp.message_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.message_service.dto.MessageDto;
import com.whatsapp.message_service.models.Message;
import com.whatsapp.message_service.models.Status;
import com.whatsapp.message_service.services.MessageService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    private MockMvc mvc;
    private ObjectMapper objectMapper;
    @MockBean
    private MessageService messageService;
    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }
    @Test
    void should_send_message() throws Exception {
        MessageDto messageDto = new MessageDto("Hello World", "123456789","987654321");
        RequestBuilder request = post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto));
        
        mvc.perform(request)
            .andExpect(status().isCreated());
    }
    @Test
    void should_fetch_message() throws Exception {
        RequestBuilder request = get("/api/v1/messages/a@g.com");
        Message message =new Message(0l, "Hello World!", "ayush", "a@g.com", null, Status.RECEIVED_BY_SERVER);
        when(messageService.findMessages("a@g.com"))
            .thenReturn(List.of(message));
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value("Hello World!"))
            .andExpect(jsonPath("$[0].sentBy").value("ayush"))
            .andExpect(jsonPath("$[0].sentTo").value("a@g.com"))
            .andExpect(jsonPath("$").isArray());
    }
}

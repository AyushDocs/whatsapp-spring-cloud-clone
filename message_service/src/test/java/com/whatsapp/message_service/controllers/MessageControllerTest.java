package com.whatsapp.message_service.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.message_service.dto.MessageDto;
import com.whatsapp.message_service.services.MessageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


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
    @ParameterizedTest()
    @CsvSource({ "987654321","atfyny","a@gjubuu","a@g."})
    void should_not_send_message_invalid_email(String email) throws Exception {
        MessageDto messageDto = new MessageDto("Hello World", email);
        RequestBuilder request = post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto));
        
        mvc.perform(request)
            .andExpect(status().isBadRequest());
    }
    @Test()
    void should_send_message() throws Exception {
        MessageDto messageDto = new MessageDto("Hello World", "a@g.com");
        RequestBuilder request = post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto));
        
        mvc.perform(request)
            .andExpect(status().isCreated());
    }
}

package com.whatsapp.message_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    private MockMvc mvc;
    @Test
    void should_send_message() throws Exception {
        RequestBuilder request = post("/api/v1/messages/")
                .param("content", "Hello World!")
                .param("sentBy", "987654321")
                .param("sentTo", "a@g.com");
        
        mvc.perform(request)
            .andExpect(status().isCreated());
    }
    @Test
    void should_fetch_message() throws Exception {
        RequestBuilder request = get("/api/v1/messages/")
                .param("sentBy", "987654321")
                .param("sentTo", "a@g.com");
        
        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value("Hello World!"))
            .andExpect(jsonPath("$[0].timestamp").value("28/10/5"))
            .andExpect(jsonPath("$").isArray());
    }
}

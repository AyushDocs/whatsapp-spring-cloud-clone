package com.whatsapp.profile_service.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.profile_service.dto.UserDto;
import com.whatsapp.profile_service.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void should_signup_successfully() throws Exception {
        UserDto userDto = new UserDto("test", "test", "test@gmail.com");
        when(userService.signupAndReturnSuccessState("test@gmail.com", "test", "test")).thenReturn(true);
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isCreated());
    }
    @Test
    void should_login_successfully() throws Exception {
        UserDto userDto = new UserDto("test", "test", "test@gmail.com");
        when(userService.generateToken("test@gmail.com", "test","127.0.0.1")).thenReturn("test");
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", "test"));
    }
    @Test
    void should_not_login_successfully() throws Exception {
        UserDto fake = new UserDto("fake", "fake", "fake@gmail.com");
        when(userService.generateToken("test@gmail.com", "test","127.0.0.1")).thenReturn("test");
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fake)))
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist("token"));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}

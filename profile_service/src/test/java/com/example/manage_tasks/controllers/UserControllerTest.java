package com.example.manage_tasks.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        when(userService.signup(userDto)).thenReturn("test");
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", "test"));
    }

    @Test
    void should_not_signup_successfully_invalid_credentials() throws Exception {
        UserDto userDto = new UserDto("test", "test", "test@gmail.com");
        UserDto fake = new UserDto("fake", "fake", "fake@gmail.com");
        when(userService.signup(userDto)).thenReturn("test");
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fake)))
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist("token"));
    }
    @Test
    void should_login_successfully() throws Exception {
        UserDto userDto = new UserDto("test", "test", "test@gmail.com");
        when(userService.login(userDto)).thenReturn("test");
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", "test"));
    }
    @Test
    void should_not_login_successfully() throws Exception {
        UserDto userDto = new UserDto("test", "test", "test@gmail.com");
        UserDto fake = new UserDto("fake", "fake", "fake@gmail.com");
        when(userService.login(userDto)).thenReturn("test");
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

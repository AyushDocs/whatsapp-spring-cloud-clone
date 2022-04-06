package com.whatsapp.profile_service.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.profile_service.configuration.JwtConfig;
import com.whatsapp.profile_service.dto.LoginRequest;
import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.services.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService userService;
    @MockBean
    private JwtConfig jwtConfig;
    
    @Test
    void should_signup_successfully() throws Exception {
        SignupRequest userDto = new SignupRequest("test", "test", "test@gmail.com");

        MockHttpServletRequestBuilder request = post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }
    @Test
    void should_login_successfully() throws Exception {
        LoginRequest userDto = new LoginRequest("test","test@gmail.com");
        String cookieName ="token";
        when(jwtConfig.getCookieName()).thenReturn(cookieName);
        when(userService.generateToken("test@gmail.com", "test","127.0.0.1")).thenReturn("test");
        MockHttpServletRequestBuilder request = post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(cookie().exists(cookieName))
                .andExpect(cookie().value(cookieName, "test"));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}

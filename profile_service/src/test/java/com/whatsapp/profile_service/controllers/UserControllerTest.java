package com.whatsapp.profile_service.controllers;

import static org.mockito.Mockito.verify;

import com.whatsapp.profile_service.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
      @Autowired
      private MockMvc mvc;
      @MockBean private UserService userService;
      @Test
      void should_add_friend() throws Exception{
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/users/{userId}/{friendId}",1,2);
            mvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated());
            verify(userService).addFriend(1l,2l);
      }
}

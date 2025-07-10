package com.whatsapp.profile_service.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsapp.profile_service.dto.FriendRequest;
import com.whatsapp.profile_service.dto.ModifyUserRequest;
import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.services.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
      @Autowired
      private MockMvc mvc;
      @MockBean
      private UserService userService;
      private ObjectMapper mapper=new ObjectMapper();
      
      @Test
      void should_find_new_friends_with_email() throws Exception {
            Response<Page<User>> res = getPageResponseOfUsers();
            FriendRequest friendRequestObj = FriendRequest.builder("test@example.com",1,0);
            when(userService.findNewFriends(any(FriendRequest.class))).thenReturn(res);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .get("/api/v1/users/?text=test@example.com&page=1&offset=0");
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isPartialContent())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray());
            ArgumentCaptor<FriendRequest> ar = ArgumentCaptor.forClass(FriendRequest.class);
            verify(userService).findNewFriends(ar.capture());
            assertEquals(friendRequestObj, ar.getValue());

      }
      @Test
      void should_update_user() throws Exception {
            ModifyUserRequest modifyUserRequest = ModifyUserRequest
            .builder().name("test").email("a@g.com").build();
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .put("/api/v1/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(modifyUserRequest));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isNoContent());
            verify(userService).updateUser(1,modifyUserRequest);
      }
      @Test
      void should_not_update_user_no_user_with_given_id() throws Exception {
            ModifyUserRequest modifyUserRequest = ModifyUserRequest
            .builder().name("test").email("a@g.com").build();
            doThrow(UserNotFoundException.class).when(userService).updateUser(1,modifyUserRequest);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .put("/api/v1/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(modifyUserRequest));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with id 1 not found"));
            verify(userService).updateUser(1,modifyUserRequest);
      }

      private Response<Page<User>> getPageResponseOfUsers() {
            User user1 = new User();
            User user2 = new User();
            User user3 = new User();
            List<User> users = List.of(user1, user2, user3);
            Page<User> userPages = new PageImpl<User>(users);
            Response<Page<User>> res =new Response<Page<User>>(userPages,null,false);
            return res;
      }
}

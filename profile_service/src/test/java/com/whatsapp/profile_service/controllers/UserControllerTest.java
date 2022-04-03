package com.whatsapp.profile_service.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.whatsapp.profile_service.dto.FriendRequest;
import com.whatsapp.profile_service.dto.Response;
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

      @Test
      void should_add_friend() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/users/{userId}/{friendId}", 1,
                        2);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isCreated());
            verify(userService).addFriend(1l, 2l);
      }

      @Test
      void should_find_new_friends_with_email() throws Exception {
            Response<Page<User>> res = getPageResponseOfUsers();
            FriendRequest friendRequestObj = FriendRequest.builder()
                        .textInput("test@example.com")
                        .page(1)
                        .offset(0)
                        .build();
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

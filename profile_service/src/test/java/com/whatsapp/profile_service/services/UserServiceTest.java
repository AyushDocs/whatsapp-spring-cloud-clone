package com.whatsapp.profile_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.whatsapp.profile_service.dto.FriendRequest;
import com.whatsapp.profile_service.dto.ModifyUserRequest;
import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class UserServiceTest {
      @MockBean
      private UserRepository userRepository;
      private UserService underTest;

      @BeforeEach
      void setUp() {
            underTest = new UserService(userRepository);
      }
      @Test
      void should_find_friends_by_email() {
            List<User> users = getDummyUsers();
            Page<User> page = new PageImpl<>(users, PageRequest.of(1, 10), 2);
            when(userRepository.findAllByEmailContainingOrNameContaining("email1", "email1", PageRequest.of(1, 10)))
                        .thenReturn(page);
            FriendRequest request = getDummyFriendRequest();
            Response<Page<User>> friends = underTest.findNewFriends(request);
            assertEquals(2, friends.getData().toList().size());
            assertTrue(friends.getData() instanceof Page<User>);
            verify(userRepository).findAllByEmailContainingOrNameContaining("email1", "email1", PageRequest.of(1, 10));
      }
      @Test
      void should_update_user() {
            // given
            User user = new User("name","password","email");
            user.setId(1l);
            user.setImageUrl("http://a.com/5");
            when(userRepository.findById(1l)).thenReturn(Optional.of(user));
            ModifyUserRequest modifyUserRequest = ModifyUserRequest
                        .builder().name("updatedName").email("updatedEmail").imageUrl("http://a.com/6").build();
            // when
            underTest.updateUser(1l, modifyUserRequest);
            // then
            verify(userRepository).findById(1l);
            ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(argumentCaptor.capture());
            User user1 = argumentCaptor.getValue();
            assertEquals(1l, user1.getId());
            assertEquals("updatedName", user1.getName());
            assertEquals("updatedEmail", user1.getEmail());
            assertEquals("http://a.com/6", user1.getImageUrl());
      }
      @Test
      void should_not_update_user_no_user_with_given_id() {
            when(userRepository.findById(1l)).thenReturn(Optional.ofNullable(null));
            ModifyUserRequest modifyUserRequest = ModifyUserRequest
                        .builder().name("updatedName").email("updatedEmail").build();
            // when
            assertThrowsExactly(UserNotFoundException.class,()-> underTest.updateUser(1l, modifyUserRequest));
           
            // then
            
            verify(userRepository).findById(1l);
      }

      private FriendRequest getDummyFriendRequest() {
            FriendRequest request = FriendRequest.builder("email1", 10, 1);
            return request;
      }

      private List<User> getDummyUsers() {
            return List.of(
                        new User("username1", "password1", "email1"),
                        new User("username2", "password2", "email2"));
      }
}

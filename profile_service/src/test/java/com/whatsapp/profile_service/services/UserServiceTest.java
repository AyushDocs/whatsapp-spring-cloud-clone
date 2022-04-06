package com.whatsapp.profile_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.whatsapp.profile_service.dto.FriendRequest;
import com.whatsapp.profile_service.dto.Response;
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
      void should_add_friend() {
            // given
            User userOne = new User();
            User userTwo = new User();
            userOne.setUserId(1l);
            userTwo.setUserId(2l);
            when(userRepository.findAllById(List.of(1l, 2l))).thenReturn(List.of(userOne, userTwo));

            // when
            underTest.addFriend(1l, 2l);
            // then
            verify(userRepository).findAllById(List.of(1l, 2l));
            ArgumentCaptor<Iterable<User>> argumentCaptor = ArgumentCaptor.forClass(Iterable.class);
            verify(userRepository).saveAll(argumentCaptor.capture());
            List<User> users = (List<User>) argumentCaptor.getValue();
            User user1 = users.get(0);
            User user2 = users.get(1);
            assertTrue(user1.getFriends().contains(user2));
            assertTrue(user2.getFriends().contains(user1));
            assertEquals(2, users.size());
            assertEquals(1l, user1.getUserId());
            assertEquals(2l, user2.getUserId());
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

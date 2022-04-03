package com.whatsapp.profile_service.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.whatsapp.profile_service.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class UserRepositoryTest {
      @Autowired private TestEntityManager em;
      @Autowired private UserRepository underTest;
      @BeforeEach
      void tearDown(){
            em.clear();
      }
      @ParameterizedTest
      @ValueSource(strings = {"username1","username2", "username3"})
      void should_find_1friend_with_username(String text) {
            List<User> users = List.of(
                  new User("username1", "password1", "email1"),
                  new User("username2", "password2", "email2"),
                  new User("username3", "password3", "email3")
            );
            users.forEach(em::persist);
            int index = Integer.parseInt(text.substring(text.length() - 1))-1;
            Page<User> friends = underTest.findAllByEmailContainingOrNameContaining(text,text, PageRequest.of(0, 10));
            assertEquals(1,friends.stream().count());
            assertEquals(users.get(index),friends.getContent().get(0));
      }
      @ParameterizedTest
      @ValueSource(strings = {"username1"})
      void should_find_2friends_with_username(String text) {
            List<User> users = List.of(
                  new User("username1", "password1", "email1"),
                  new User("username12", "password2", "email2"),
                  new User("username3", "password3", "email3")
            );
            users.forEach(em::persist);

            Page<User> friends = underTest.findAllByEmailContainingOrNameContaining(text,text, PageRequest.of(0, 10));
            assertEquals(2,friends.toList().size());
            assertEquals(users.get(0),friends.getContent().get(0));
      }
      @ParameterizedTest
      @ValueSource(strings = {"email1","email2", "email3"})
      void should_find_1friend_with_email(String text) {
            List<User> users = List.of(
                  new User("username1", "password1", "email1"),
                  new User("username2", "password2", "email2"),
                  new User("username3", "password3", "email3")
            );
            int index=Integer.parseInt(text.substring(text.length()-1))-1;
            users.forEach(em::persist);

            Page<User> friends = underTest.findAllByEmailContainingOrNameContaining(text,text, PageRequest.of(0, 10));
            assertEquals(1,friends.toList().size());
            assertEquals(users.get(index),friends.getContent().get(0));
      }
      @ParameterizedTest
      @ValueSource(strings = {"email1"})
      void should_find_2friends_with_email(String text) {
            List<User> users = List.of(
                  new User("username1", "password1", "email1"),
                  new User("username2", "password2", "email12"),
                  new User("username3", "password3", "email3")
            );
            users.forEach(em::persist);
            int index = Integer.parseInt(text.substring(text.length() - 1));

            Page<User> friends = underTest.findAllByEmailContainingOrNameContaining(text,text, PageRequest.of(0, 10));
            assertEquals(2,friends.toList().size());
            assertEquals(users.get(index),friends.getContent().get(index));
      }
}

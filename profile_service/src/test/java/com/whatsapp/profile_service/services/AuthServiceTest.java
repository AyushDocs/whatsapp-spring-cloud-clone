package com.whatsapp.profile_service.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.whatsapp.profile_service.dto.UserDto;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class AuthServiceTest {
    @MockBean private UserRepository repository;
    @MockBean private JwtUtils jwtUtils;
    @MockBean private PasswordEncoder passwordEncoder;
    @Autowired private AuthService underTest;
    @Test
    void should_login() {
        UserDto userDto=new UserDto("ayush","123456","ayush@gmail.com");
        User user=new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token=underTest.generateToken(userDto.getEmail(),userDto.getPassword(),"127.0.0.1");
        verify(repository).findByEmail(userDto.getEmail());
        assertEquals( "token",token);
    }
    @Test
    void should_not_login_invalid_passwd() {
        UserDto userDto=new UserDto("ayush","23456","ayush@gmail.com");
        User user=new User("ayush", "12345", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token= underTest.generateToken(userDto.getEmail(), userDto.getPassword(), "127.0.0.1");
        assertNull(token);
    }
    @Test
    void should_not_login_invalid_email() {
        UserDto userDto=new UserDto("ayush","123456","ayushgmail.com");
        User user=new User("ayush", "123456", "ayushgmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token= underTest.generateToken(userDto.getEmail(), userDto.getPassword(), "127.0.0.1");
        verify(repository,never()).findByEmail(userDto.getEmail());
        assertNull(token);
    }

    @Test
    void should_signup() {
        UserDto userDto=new UserDto("ayush","123456","ayush@gmail.com");
        User user=new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.encode("123456")).thenReturn("123456");
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        boolean success=underTest.signupAndReturnSuccessState(userDto.getEmail(), userDto.getPassword(),userDto.getUsername());
        verify(repository).existsByEmail("ayush@gmail.com");
        ArgumentCaptor<User> ac=ArgumentCaptor.forClass(User.class);
        verify(repository).save(ac.capture());
        assertAll(() -> {
            User stored=ac.getValue();
            assertEquals(stored.getName(), user.getName());
            assertEquals(stored.getEmail(), user.getEmail());
            assertEquals(stored.getPassword(), user.getPassword());
        });
        assertTrue(success);
    }
    
    @Test
    void should_not_signup_invalid_passwd() {
        User user = new User( "ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        boolean success = underTest.signupAndReturnSuccessState("ayush", "12345","ayush");
        assertFalse(success);
    }

    @Test
    void should_not_signup_invalid_email() {
        UserDto userDto = new UserDto("ayush", "123456", "ayushgmail.com");
        User user = new User("ayush", "123456", "ayushgmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        boolean success = underTest.signupAndReturnSuccessState(userDto.getEmail(), userDto.getPassword(),
                userDto.getUsername());
        verify(repository, never()).existsByEmail(userDto.getEmail());
        assertFalse(success);
    }
    @Test
    void should_not_signup_invalid_similar_user_exists() {
        UserDto userDto = new UserDto("ayush", "123456", "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        boolean success = underTest.signupAndReturnSuccessState(userDto.getEmail(), userDto.getPassword(),
                userDto.getUsername());
        verify(repository).existsByEmail(userDto.getEmail());
        assertFalse(success);

    }
}

package com.whatsapp.profile_service.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.whatsapp.profile_service.dto.LoginRequest;
import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.exceptions.RequestValidationException;
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
    @MockBean
    private UserRepository repository;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService underTest;

    @Test
    void should_login() {
        SignupRequest userDto = new SignupRequest("ayush", "123456", "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token = underTest.generateToken(userDto.getEmail(), userDto.getPassword(), "127.0.0.1");
        verify(repository).findByEmail(userDto.getEmail());
        assertEquals("token", token);
    }

    @Test
    void should_not_login_invalid_passwd() {
        LoginRequest userDto = new LoginRequest("23456", "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("2345", "123456")).thenReturn(false);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        assertThrowsExactly(RequestValidationException.class,
        ()->underTest.generateToken(userDto.getEmail(), userDto.getPassword(), "127.0.0.1") ) ;
       
    }

    @Test
    void should_signup() {
        SignupRequest userDto = new SignupRequest("ayush", "123456", "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.encode("123456")).thenReturn("123456");
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        assertDoesNotThrow(() -> underTest.signup(userDto.getEmail(), userDto.getPassword(), userDto.getUsername()));
        verify(repository).existsByEmail("ayush@gmail.com");
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        verify(repository).save(ac.capture());
        assertAll(() -> {
            User stored = ac.getValue();
            assertEquals(stored.getName(), user.getName());
            assertEquals(stored.getEmail(), user.getEmail());
            assertEquals(stored.getPassword(), user.getPassword());
        });
    }

    @Test
    void should_not_signup_invalid_passwd() {
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
        when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        assertThrowsExactly(RequestValidationException.class,() -> underTest.signup("ayush@gmail.com", "12345", "ayush"));
    }

    @Test
    void should_not_signup_invalid_similar_user_exists() {
        SignupRequest userDto = new SignupRequest("ayush", "123456", "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        assertThrowsExactly(RequestValidationException.class,
                () -> underTest.signup(userDto.getEmail(), userDto.getPassword(),
                        userDto.getUsername()));
        verify(repository).existsByEmail(userDto.getEmail());

    }
}

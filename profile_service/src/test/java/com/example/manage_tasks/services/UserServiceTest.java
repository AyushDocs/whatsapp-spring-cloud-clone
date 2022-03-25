package com.example.manage_tasks.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.models.User;
import com.example.manage_tasks.repositories.UserRepository;
import com.example.manage_tasks.utils.JwtUtils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {
    @MockBean private UserRepository repository;
    @MockBean private JwtUtils jwtUtils;
    @MockBean private PasswordEncoder passwordEncoder;
    @Autowired private UserService underTest;
    @Test
    void should_login() {
        UserDto userDto=new UserDto("ayush","123456","ayush@gmail.com");
        User user=new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token=underTest.login(userDto);
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
        String token=underTest.login(userDto);
        assertNull(token);
    }
    @Test
    void should_not_login_invalid_email() {
        UserDto userDto=new UserDto("ayush","123456","ayushgmail.com");
        User user=new User("ayush", "123456", "ayushgmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token=underTest.login(userDto);
        verify(repository,never()).findByEmail(userDto.getEmail());
        assertNull(token);
    }

    @Test
    void should_signup() {
        UserDto userDto=new UserDto("ayush","123456","ayush@gmail.com");
        User user=new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.encode("123456")).thenReturn("123456");
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token=underTest.signup(userDto);
        verify(repository).existsByEmail("ayush@gmail.com");
        verify(repository).save(user);
        assertEquals("token", token);
    }
    
    @Test
    void should_not_signup_invalid_passwd() {
        UserDto userDto = new UserDto("ayush", "23456", "ayush@gmail.com");
        User user = new User("ayush", "12345", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token = underTest.signup(userDto);
        assertNull(token);
    }

    @Test
    void should_not_signup_invalid_email() {
        UserDto userDto = new UserDto("ayush", "123456", "ayushgmail.com");
        User user = new User("ayush", "123456", "ayushgmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token = underTest.signup(userDto);
        verify(repository, never()).existsByEmail(userDto.getEmail());
        assertNull(token);
    }
    @Test
    void should_not_signup_invalid_similar_user_exists() {
        UserDto userDto = new UserDto("ayush", "123456", "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("token");
        String token = underTest.signup(userDto);
        verify(repository).existsByEmail(userDto.getEmail());
        assertNull(token);
    }
}

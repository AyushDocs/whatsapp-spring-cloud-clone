package com.whatsapp.profile_service.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.whatsapp.profile_service.configuration.JwtConfig;
import com.whatsapp.profile_service.dto.LoginRequest;
import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.exceptions.RequestValidationException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RateLimiterService rateLimiterService;
    @MockBean
    private JwtConfig jwtConfig;
    private AuthService underTest;

    @BeforeEach
    void setup() {
        underTest = new AuthService(repository, jwtUtils, passwordEncoder, rateLimiterService, restTemplate);
    }

    @Test
    void should_login() {
        SignupRequest userDto = new SignupRequest("ayush", "123456", null, "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(repository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("token");
        when(rateLimiterService.isEligibleForLogin(any(String.class))).thenReturn(true);
        String token = underTest.generateToken(userDto.getEmail(), userDto.getPassword(), "127.0.0.1");
        verify(repository).findByEmail(userDto.getEmail());
        assertEquals("token", token);
    }

    @Test
    void should_not_login_invalid_passwd() {
        LoginRequest userDto = new LoginRequest("23456", "ayush@gmail.com");
        assertThrowsExactly(RequestValidationException.class,
                () -> underTest.generateToken(userDto.getEmail(), userDto.getPassword(), "127.0.0.1"));

    }

    @Test 
    @SuppressWarnings("unchecked")
    void should_signup() throws RestClientException, URISyntaxException {
        MultipartFile file = null;
        SignupRequest userDto = new SignupRequest("ayush", "123456", file, "ayush@gmail.com");
        User user = new User("ayush", "123456", "ayush@gmail.com");
        user.setUserId(5l);
        when(passwordEncoder.encode("123456")).thenReturn("123456");
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);
        when(restTemplate.postForEntity("http://IMAGE-SERVICE/api/v1/images/{}",
                file,
                Void.class, 5l))
                .thenReturn(ResponseEntity.status(200).location(new URI("http://localhost:8080/users")).build());
        assertDoesNotThrow(
                () -> underTest.signup(userDto.getEmail(), userDto.getPassword(), userDto.getUsername(), file));
        verify(repository).existsByEmail("ayush@gmail.com");
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        verify(repository, times(2)).save(ac.capture());
        assertAll(() -> {
            User stored = ac.getValue();
            assertEquals(stored.getName(), user.getName());
            assertEquals(stored.getEmail(), user.getEmail());
            assertEquals(stored.getPassword(), user.getPassword());
        });
    }

    @Test
    void should_not_signup_invalid_passwd() {
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);

        assertThrowsExactly(RequestValidationException.class,
                () -> underTest.signup("ayush@gmail.com", "12345", "ayush", null));
    }

    @Test
    void should_not_signup_invalid_similar_user_exists() {
        SignupRequest userDto = new SignupRequest("ayush", "123456", null, "ayush@gmail.com");
        when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
        assertThrowsExactly(RequestValidationException.class,
                () -> underTest.signup(userDto.getEmail(), userDto.getPassword(),
                        userDto.getUsername(), null));
        verify(repository).existsByEmail(userDto.getEmail());

    }
}

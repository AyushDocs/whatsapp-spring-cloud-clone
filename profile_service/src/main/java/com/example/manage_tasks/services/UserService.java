package com.example.manage_tasks.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.models.User;
import com.example.manage_tasks.repositories.UserRepository;
import com.example.manage_tasks.utils.JwtUtils;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public Mono<String> signup(UserDto userDto) {
        return repository.existsByEmail(userDto.getEmail())
                .map(bool -> {
                    if (bool == null || bool)
                        return null;
                    User user = new User();
                    user.setEmail(userDto.getEmail());
                    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    user.setUserName(userDto.getUsername());
                    repository.save(user);
                    return jwtUtils.generateToken(user);
                });
    }

    public Mono<String> login(UserDto userDto) {
        return repository.findByEmail(userDto.getEmail())
                .filter(Objects::nonNull)
                .map(user -> {
                    if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword()))
                        return null;
                    return user;
                })
                .map(jwtUtils::generateToken);
    }

}

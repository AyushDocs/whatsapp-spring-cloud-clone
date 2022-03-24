package com.example.manage_tasks.controllers;

import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.services.UserService;
import com.google.common.net.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @Value("${jwt.cookie_name}")
    private String jwtCookieName;
    @Value("${jwt.time_delta}")
    private Long jwtCookieMaxAge;

    @PostMapping("/signup")
    @PreFilter("hasRole('NONE')")
    public Mono<ResponseEntity<Void>> signup(@RequestBody UserDto userDto) {
        return userService.signup(userDto)
                .map(jwt -> ResponseCookie.from(jwtCookieName, jwt).httpOnly(true).maxAge(jwtCookieMaxAge)
                        .build())
                .map(cookie -> ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build());
    }

    @PostMapping("/login")
    @PreFilter("hasRole('NONE')")
    public Mono<ResponseEntity<Void>> login(@RequestBody UserDto userDto) {
        return userService.login(userDto)
                .map(jwt -> ResponseCookie.from(jwtCookieName, jwt).httpOnly(true).maxAge(jwtCookieMaxAge)
                        .build())
                .map(cookie -> ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build());
    }
}
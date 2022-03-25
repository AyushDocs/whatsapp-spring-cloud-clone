package com.example.manage_tasks.controllers;

import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.services.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<Void> signup(@RequestBody UserDto userDto) {
        String jwt = userService.signup(userDto);
        if (jwt == null)
            return ResponseEntity.badRequest().build();
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt)
                .httpOnly(true)
                .maxAge(jwtCookieMaxAge)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/login")
    @PreFilter("hasRole('NONE')")
    public ResponseEntity<Void> login(@RequestBody UserDto userDto) {
        String jwt = userService.login(userDto);
        if (jwt == null)
            return ResponseEntity.badRequest().build();
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwt)
                .httpOnly(true)
                .maxAge(jwtCookieMaxAge)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    // String jwt = jwtUtils.generateToken(user);
    // ResponseCookie cookie=ResponseCookie.from(JWT_COOKIE_NAME,
    // jwt).httpOnly(true).maxAge(JWT_COOKIE_MAX_AGE).build();return
    // ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).build();
}
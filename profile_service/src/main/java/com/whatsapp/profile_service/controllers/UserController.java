package com.whatsapp.profile_service.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.whatsapp.profile_service.dto.UserDto;
import com.whatsapp.profile_service.services.UserService;

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
        String username=userDto.getUsername();
        String email=userDto.getEmail();
        String password=userDto.getPassword();
        boolean successState=userService.signupAndReturnSuccessState(email,password,username);       
        return ResponseEntity
                .status(successState?
                         HttpStatus.CREATED:
                         HttpStatus.BAD_REQUEST)
                .build();
    }

    @PostMapping("/login")
    @PreFilter("hasRole('NONE')")
    public ResponseEntity<Void> login(@RequestBody UserDto userDto,HttpServletRequest request) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Optional<String> jwtOpt =Optional.ofNullable(userService.generateToken(email,password,request.getRemoteAddr())) ;
        if (jwtOpt.isEmpty()) return ResponseEntity.badRequest().build();
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, jwtOpt.get())
                .httpOnly(true)
                .maxAge(jwtCookieMaxAge)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
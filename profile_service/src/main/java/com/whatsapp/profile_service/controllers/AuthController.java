package com.whatsapp.profile_service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.whatsapp.profile_service.configuration.JwtConfig;
import com.whatsapp.profile_service.dto.LoginRequest;
import com.whatsapp.profile_service.dto.SignupRequest;
import com.whatsapp.profile_service.services.AuthService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AuthController {
    private final AuthService userService;
    private final JwtConfig jwtConfig;

    @PostMapping("/signup")
    @PreFilter("hasRole('NONE')")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid SignupRequest userDto) {
        String username = userDto.getUsername();
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        userService.signup(email, password, username);
    }

    @PostMapping("/login")
    @PreFilter("hasRole('NONE')")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String ip = request.getRemoteAddr();

        String jwt = userService.generateToken(email, password, ip);
        ResponseCookie cookie = createCookieWithJwtInIt(jwt);
        return createResponseWithCookieInIt(cookie);
    }

    private ResponseEntity<Void> createResponseWithCookieInIt(ResponseCookie cookie) {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie createCookieWithJwtInIt(String jwt) {
        return ResponseCookie.from(jwtConfig.getCookieName(), jwt)
                .httpOnly(true)
                .maxAge(900000l)
                .secure(true)
                .sameSite("None")
                .path("/")
                .build();

    }
}
package com.example.manage_tasks.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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


@Service
public class UserService {

    private final String JWT_COOKIE_NAME;
    private final Duration JWT_COOKIE_MAX_AGE;
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

   public UserService(UserRepository repository,JwtUtils jwtUtils,AuthenticationManager authenticationManager,PasswordEncoder passwordEncoder,Environment env){
        this.repository = repository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.JWT_COOKIE_MAX_AGE = Duration.ofSeconds(env.getRequiredProperty("jwt.time_delta",Long.class));
        this.JWT_COOKIE_NAME = env.getRequiredProperty("jwt.cookie_name",String.class);
    }

    public ResponseEntity signup(UserDto userDto){
        User prevUser=repository.findByEmail(userDto.getEmail()).orElse(null);
        if (prevUser!=null) return ResponseEntity.badRequest().body("User already exists");
        User user=new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getUsername());
        user=repository.save(user);
        String jwt=jwtUtils.generateToken(user);
        ResponseCookie cookie=ResponseCookie.from(JWT_COOKIE_NAME, jwt).httpOnly(true).maxAge(JWT_COOKIE_MAX_AGE).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).build();
    }
    public ResponseEntity login(UserDto userDto){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        String jwt=jwtUtils.generateToken((User)authentication.getPrincipal());
        System.out.println(jwt);
        ResponseCookie cookie = ResponseCookie.from(JWT_COOKIE_NAME, jwt).httpOnly(true).maxAge(JWT_COOKIE_MAX_AGE)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }


}

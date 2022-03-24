package com.example.manage_tasks.controllers;
import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController{
    private final UserService userService;
    @PostMapping("/signup")
    @PreFilter("hasRole('NONE')")
    public ResponseEntity signup(@RequestBody UserDto userDto){
        System.out.println("reached here");
        return userService.signup(userDto);
    }
    @PostMapping("/login")
    @PreFilter("hasRole('NONE')")
    public ResponseEntity login(@RequestBody UserDto userDto){
        System.out.println('\n'+"reached here");
        return userService.login(userDto);
    }
}
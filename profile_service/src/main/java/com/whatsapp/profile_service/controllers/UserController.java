package com.whatsapp.profile_service.controllers;

import com.whatsapp.profile_service.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
      private final UserService userService;
      @PostMapping("{userId}/{friendId}")
      @ResponseStatus(HttpStatus.CREATED)
      public void addFriend(@PathVariable Long userId,@PathVariable Long friendId){
            userService.addFriend(userId, friendId);
      }
}

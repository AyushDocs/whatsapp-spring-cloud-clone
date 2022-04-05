package com.whatsapp.profile_service.controllers;

import com.whatsapp.profile_service.dto.FriendRequest;
import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.services.UserService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
            userService.addFriend(userId, friendId);
      }

      @GetMapping
      public ResponseEntity<Response<Page<User>>> findNewFriends(
                  @RequestParam String text,
                  @RequestParam(defaultValue = "10") Integer page,
                  @RequestParam(defaultValue = "0") Integer offset) {
            FriendRequest request = FriendRequest.builder(text, page, offset);
            Response<Page<User>> response = userService.findNewFriends(request);

            if (response.getData().isEmpty())
                  return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
      }
}

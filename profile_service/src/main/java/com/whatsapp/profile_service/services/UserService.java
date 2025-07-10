package com.whatsapp.profile_service.services;

import com.whatsapp.profile_service.dto.FriendRequest;
import com.whatsapp.profile_service.dto.ModifyUserRequest;
import com.whatsapp.profile_service.dto.Response;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

      /**
       *
       */
      private static final String NO_USER_ERR_MESSAGE = "User with id %s not found";
      private final UserRepository userRepository;

      public Response<Page<User>> findNewFriends(FriendRequest request) {
            PageRequest page = PageRequest.of(request.getOffset(), request.getPage());
            String text = request.getTextInput();
            Page<User> friends = userRepository.findAllByEmailContainingOrNameContaining(text, text, page);
            return new Response<>(friends, null, false);
      }

      public void updateUser(long id, ModifyUserRequest modifyUserRequest) {
            User user = userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(NO_USER_ERR_MESSAGE.formatted(id)));
            if (!modifyUserRequest.getEmail().equals(user.getEmail()))
                  user.setEmail(modifyUserRequest.getEmail());
            if (!modifyUserRequest.getName().equals(user.getName()))
                  user.setName(modifyUserRequest.getName());
            if (!modifyUserRequest.getImageUrl().equals(user.getImageUrl()))
                  user.setImageUrl(modifyUserRequest.getImageUrl());
            userRepository.save(user);
      }
}

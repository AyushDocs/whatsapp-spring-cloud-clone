package com.whatsapp.profile_service.services;

import java.util.List;

import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

      private final UserRepository userRepository;

      public void addFriend(Long userId, Long friendId) {
            List<User> users = userRepository.findAllById(List.of(userId, friendId));
            User user = users.get(0);
            User friend = users.get(1);
            user.getFriends().add(friend);
            friend.getFriends().add(user);
            userRepository.saveAll(users);
      }

}

package com.example.manage_tasks.services;

import com.example.manage_tasks.dto.UserDto;
import com.example.manage_tasks.exceptions.UserNotFoundException;
import com.example.manage_tasks.models.User;
import com.example.manage_tasks.repositories.UserRepository;
import com.example.manage_tasks.utils.JwtUtils;
import com.example.manage_tasks.validators.EmailValidator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;

    public String signup(UserDto userDto) {
        if(!emailValidator.test(userDto.getEmail()))return null;
        boolean userExists = repository.existsByEmail(userDto.getEmail());
        if (userExists) return null;
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getUsername());
        repository.save(user);
        return jwtUtils.generateToken(user);
    }

    public String login(UserDto userDto) {
        if (!emailValidator.test(userDto.getEmail()))
            return null;

        User user = repository.findByEmail(userDto.getEmail()).orElseThrow(UserNotFoundException::new);
        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) return null;
        return jwtUtils.generateToken(user);
    }
}

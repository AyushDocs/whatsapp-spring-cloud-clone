package com.whatsapp.profile_service.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.whatsapp.profile_service.dto.UserDto;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;
import com.whatsapp.profile_service.validators.EmailValidator;

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
    private final RateLimiterService rateLimiterService;

    public boolean signupAndReturnSuccessState(String email, String password, String username) {
        if (!emailValidator.test(email)) return false;
        boolean userExists = repository.existsByEmail(email);
        if (userExists) return false;
        saveUser(email, password, username);
        return true;
    }

    
    public String generateToken(String email, String password,String ip) {
        if (!rateLimiterService.isEligibleForLogin(ip)) return null;
        if (!emailValidator.test(email)) return null;
        User user = fetchUser(email,ip);
        boolean passwordMatches=checkIfPasswordsAreCorrect(user,password,ip);
        if(!passwordMatches) return null;
        updateLastLoggedInAtForUser(user);
        return jwtUtils.generateToken(user);
    }

    private void saveUser(String email, String password, String username) {
        LocalDate todayDate = LocalDate.now();
        String encodedPassword=passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword, email, todayDate,
                todayDate, "NONE", LocalDateTime.now());
        repository.save(user);
    }
    
    private void updateLastLoggedInAtForUser(User user) {
        user.setLastLoggedInAt(LocalDateTime.now());
        repository.save(user);
        rateLimiterService.clear(user.getEmail());
    }

    private boolean checkIfPasswordsAreCorrect(User user, String rawPassword,String ip) {
        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!matches) rateLimiterService.incrementTries(ip);
        return matches;
    }

    private User fetchUser(String email,String ip) {
        return repository
        .findByEmail(email)
        .orElseThrow(() -> {
            rateLimiterService.incrementTries(ip);
            return new UserNotFoundException();
        });
    }
}
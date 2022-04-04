package com.whatsapp.profile_service.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.whatsapp.profile_service.exceptions.RequestValidationException;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RateLimiterService rateLimiterService;

    public void signup(String email, String password, String username) {
        checkIfUserExistsInDb(email);
        saveUser(email, password, username);
    }


    private void checkIfUserExistsInDb(String email) {
        boolean userExists = repository.existsByEmail(email);
        if (userExists)throw new RequestValidationException("User with similar credentials already exists");
    }

    
    public String generateToken(String email, String password,String ip) {
        if (!rateLimiterService.isEligibleForLogin(ip)) throw new RequestValidationException("You have used up all your tries please try again later");
        User user = fetchUserFromDb(email,ip);
        validateUserCredentials(password, ip, user);
        updateLastLoggedInAtForUser(user);
        return jwtUtils.generateToken(user);
    }


    private void validateUserCredentials(String password, String ip, User user) {
        boolean passwordMatches=checkIfPasswordsAreCorrect(user,password,ip);
        if(!passwordMatches) throw new RequestValidationException("Incorrect credentials provided");
    }

    private void saveUser(String email, String password, String username) {
        LocalDate todayDate = LocalDate.now();
        String encodedPassword=passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword, email, todayDate,
                todayDate, "NONE", LocalDateTime.now(),null);
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

    private User fetchUserFromDb(String email,String ip) {
        return repository
        .findByEmail(email)
        .orElseThrow(() -> {
            rateLimiterService.incrementTries(ip);
            return new UserNotFoundException();
        });
    }
}
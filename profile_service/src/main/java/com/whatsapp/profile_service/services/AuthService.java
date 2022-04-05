package com.whatsapp.profile_service.services;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.whatsapp.profile_service.exceptions.RequestValidationException;
import com.whatsapp.profile_service.exceptions.UserNotFoundException;
import com.whatsapp.profile_service.models.User;
import com.whatsapp.profile_service.repositories.UserRepository;
import com.whatsapp.profile_service.utils.JwtUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RateLimiterService rateLimiterService;
    private final RestTemplate restTemplate;

    public void signup(String email, String password, String username, MultipartFile file) {
        if (userExistsInDb(email))
            throw new RequestValidationException("User with similar credentials already exists");
        saveUserAndImage(email, password, username, file);
    }

    public String generateToken(String email, String password, String ip) {
        checkIfIpHasExceededHisTries(ip);
        User user = fetchUserFromDb(email, ip);
        if (userCredentialsIsValid(password, ip, user))
            return updateUserAndCreateJwtToken(user);
        throw new RequestValidationException("Incorrect credentials provided");
    }

    private void checkIfIpHasExceededHisTries(String ip) {
        if (!rateLimiterService.isEligibleForLogin(ip))
            throw new RequestValidationException("You have used up all your tries please try again later");
    }
    private void saveUserAndImage(String email, String password, String username, MultipartFile file) {
        User user = saveUser(email, password, username);
        saveImage(file, user);
    }


    public String updateUserAndCreateJwtToken(User user) {
        updateLastLoggedInAtForUser(user);
        return jwtUtils.generateToken(user);
    }

    private void saveImage(MultipartFile file, User user) {
        URI location = sendRequestToImageService(file, user);
        updateUser(user, location);
    }

    private URI sendRequestToImageService(MultipartFile file, User user) {
        ResponseEntity<Void> response = restTemplate.postForEntity("http://IMAGE-SERVICE/api/v1/images/{}", file,
                Void.class, user.getUserId());
        return response.getHeaders()
                .getLocation();
    }

    private void updateUser(User user, URI location) {
        user.setImageUrl(location.toString());
        repository.save(user);
    }

    private boolean userExistsInDb(String email) {
        return repository.existsByEmail(email);
    }

    private boolean userCredentialsIsValid(String password, String ip, User user) {
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) rateLimiterService.incrementTries(ip);
        return matches;
    }

    private User saveUser(String email, String password, String username) {
        LocalDate todayDate = LocalDate.now();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword, email, todayDate,
                todayDate, "NONE", LocalDateTime.now(), null, null);
        return repository.save(user);
    }

    private void updateLastLoggedInAtForUser(User user) {
        user.setLastLoggedInAt(LocalDateTime.now());
        repository.save(user);
        rateLimiterService.clear(user.getEmail());
    }
    private User fetchUserFromDb(String email, String ip) {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> {
                    rateLimiterService.incrementTries(ip);
                    return new UserNotFoundException();
                });
    }
}
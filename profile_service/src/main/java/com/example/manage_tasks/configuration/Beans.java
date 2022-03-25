package com.example.manage_tasks.configuration;

import com.example.manage_tasks.exceptions.UserNotFoundException;
import com.example.manage_tasks.models.CustomUserDetails;
import com.example.manage_tasks.models.User;
import com.example.manage_tasks.repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;

@AllArgsConstructor()
@Configuration
public class Beans {
    private final UserRepository userRepository;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
            return new CustomUserDetails(
                    user);
        } ;
    }
}

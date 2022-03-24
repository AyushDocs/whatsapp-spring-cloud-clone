package com.example.manage_tasks.configuration;

import com.example.manage_tasks.exceptions.UserNotFoundException;
import com.example.manage_tasks.models.CustomUserDetails;
import com.example.manage_tasks.models.User;
import com.example.manage_tasks.repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor()
@Configuration
public class Beans {
    private final UserRepository userRepository;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return email-> userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(UserNotFoundException::new))
                .map(CustomUserDetails::new);
    }
}

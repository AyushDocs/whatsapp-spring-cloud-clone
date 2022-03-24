package com.example.manage_tasks.configuration;

import com.example.manage_tasks.models.CustomUserDetails;
import com.example.manage_tasks.utils.JwtUtils;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtUtils jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        CustomUserDetails userDetails = jwtUtil.extractUser(authToken);
        return Mono.just(jwtUtil.verifyToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(v -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }
}

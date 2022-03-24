package com.example.manage_tasks.configuration;

import com.example.manage_tasks.repositories.UserRepository;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepo implements ServerSecurityContextRepository {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("token"))
                .filter(cookie -> cookie.getValue() != null)
                .map(HttpCookie::getValue)
                .flatMap(token -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
                    return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
                });
    }

}

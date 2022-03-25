package com.example.manage_tasks.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.manage_tasks.models.CustomUserDetails;
import com.example.manage_tasks.models.User;
import com.example.manage_tasks.utils.JwtUtils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor()
public class JwtFilter extends OncePerRequestFilter {
        private final JwtUtils jwtUtils;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {
                Optional.ofNullable(request.getCookies())
                                .ifPresentOrElse(cookies -> {
                                        Arrays.stream(cookies)
                                                        .filter(i -> i.getName().equals("token"))
                                                        .findFirst()
                                                        .ifPresentOrElse(cookie -> createJwtUser(cookie, request),
                                                                        () -> createdBaseUser(request));
                                }, () -> createdBaseUser(request));
                filterChain.doFilter(request, response);
        }

        private void createJwtUser(Cookie cookie, HttpServletRequest request) {
                String token = cookie.getValue();
                if (!jwtUtils.verifyToken(token))
                        throw new RuntimeException("token not valid");
                CustomUserDetails user = jwtUtils.extractUser(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                                null,
                                user == null ? List.of(new SimpleGrantedAuthority("NONE")) : user.getAuthorities());
                authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        public void createdBaseUser(HttpServletRequest request) {
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("NONE"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null,
                                authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
        }
}

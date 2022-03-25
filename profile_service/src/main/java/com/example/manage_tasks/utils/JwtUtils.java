package com.example.manage_tasks.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.example.manage_tasks.models.CustomUserDetails;
import com.example.manage_tasks.models.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {
    @Value("${jwt.secret_key}")
    private String jwtSecret;
    @Value("${jwt.time_delta}")
    private Long jwtCookieMaxAge;

    public String generateToken(User user) {
        return Jwts
                .builder()
                .setClaims(generateClaimsMap(user))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtCookieMaxAge))
                .signWith(SignatureAlgorithm.HS256,
                        jwtSecret)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public Object extractClaim(String token, Function<Claims, Object> f) {
        return f.apply(extractClaims(token));
    }

    private Date extractExpiration(String token) {
        return (Date) extractClaim(token, Claims::getExpiration);
    }

    public boolean verifyToken(String token) {
        return extractExpiration(token).after(new Date());
    }

    private Map<String, Object> generateClaimsMap(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getName());
        return claims;
    }

    public CustomUserDetails extractUser(String token) {
        User user = new User();
        Map<String, Object> claims = extractClaims(token);
        user.setEmail((String) claims.get("email"));
        user.setName((String) claims.get("username"));
        return new CustomUserDetails(user);
    }

}

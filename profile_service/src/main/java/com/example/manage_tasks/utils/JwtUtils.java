package com.example.manage_tasks.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.example.manage_tasks.models.User;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {
    private final Long JWT_TIME_DELTA;
    private final String JWT_SECRET_KEY;

    public JwtUtils(Environment env) {
        this.JWT_SECRET_KEY=env.getRequiredProperty("jwt.secret_key",String.class);
        this.JWT_TIME_DELTA=env.getRequiredProperty("jwt.time_delta",Long.class);
    }
    public String generateToken( User user) {
        return Jwts
        .builder()
        .setClaims(generateClaimsMap(user))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TIME_DELTA))
        .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
        .compact();
    }
    private Claims extractClaims(String token){
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }
    public Object extractClaim(String token,Function<Claims,Object> f){
        return f.apply(extractClaims(token)) ;
    }
    
    private Date extractExpiration(String token){
        return (Date)extractClaim(token,Claims::getExpiration);
    }
    public boolean verifyToken(String token){
        return extractExpiration(token).after(new Date());
    }
    
    private Map<String, Object> generateClaimsMap(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        return claims;
    }
    public User extractUser(String token){
        User user = new User();
        Map<String, Object> claims = extractClaims(token);
        user.setEmail((String)claims.get("email"));
        user.setName((String)claims.get("username"));
        return user;
    }

}

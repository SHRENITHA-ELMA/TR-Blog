package com.epam.user.management.application.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    long getExpirationTime();

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    io.jsonwebtoken.Claims extractAllClaims(String token);

    java.security.Key getSignInKey();

    void blacklistToken(String token);

    boolean isTokenBlacklisted(String token);
}



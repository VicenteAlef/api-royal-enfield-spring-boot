package com.vicentedev.api_re.service;

import com.vicentedev.api_re.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String generateToken(User user);

    String generateToken(Map<String, Object> extraClaims, User user);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    long getExpirationTimeMs();
}

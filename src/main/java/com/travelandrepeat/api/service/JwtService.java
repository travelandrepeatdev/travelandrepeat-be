package com.travelandrepeat.api.service;

import com.travelandrepeat.api.conf.JwtConfig;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    public String generateToken(User user) {

        List<String> roles = new ArrayList<>(UserLoginDetails.extractRoleListFromUser(user));
        List<String> permissions = new ArrayList<>(UserLoginDetails.extractPermissionListFromUser(user));
        roles.addAll(permissions);

        return Jwts.builder()
                .subject(user.getUserId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getDisplayName())
                .claim("avatarUrl", user.getAvatarUrl())
                .claim("isActive", user.getIsActive())
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(jwtConfig.getExpiration())))
                .signWith(jwtConfig.signingKey())
                .compact();
    }

    public Claims parseToken(String token) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtConfig.signingKey().getEncoded(), jwtConfig.signingKey().getAlgorithm());
        return Jwts.parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
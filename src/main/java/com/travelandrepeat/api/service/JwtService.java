package com.travelandrepeat.api.service;

import com.travelandrepeat.api.conf.JwtConfig;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Autowired
    private JwtConfig jwtConfig;

    public String generateToken(User user) {

        List<String> roles = new ArrayList<>(UserLoginDetails.extractRoleListFromUser(user));
        List<String> permissions = new ArrayList<>(UserLoginDetails.extractPermissionListFromUser(user));
        roles.addAll(permissions);

        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getDisplayName())
                .claim("avatarUrl", user.getAvatarUrl())
                .claim("isActive", user.getIsActive())
                .claim("roles", roles)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(jwtConfig.getExpiration())))
                .signWith(jwtConfig.signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
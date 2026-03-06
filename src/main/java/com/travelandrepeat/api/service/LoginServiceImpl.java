package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLogged;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.travelandrepeat.api.dto.Role.*;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User userResult = userService.getUserByEmail(loginRequest.email());

        if (userResult == null) return null;

        if (!passwordEncoder.matches(loginRequest.password(), userResult.getHashedPassword()))
            return buildLoginResponse(userResult, null);

        String token = jwtService.generateToken(userResult);
        return buildLoginResponse(userResult, token);
    }

    @Override
    public LoginResponse profile(UserLoginDetails userLoginDetails) {
        if (userLoginDetails == null) return null;
        String role = userLoginDetails.getUser().roles().stream().filter(
                r -> r.equalsIgnoreCase(ADMIN.name()) ||
                            r.equalsIgnoreCase(AGENT.name()) ||
                            r.equalsIgnoreCase(AUDIT.name()) ||
                            r.equalsIgnoreCase(VIEWER.name()))
                .findFirst()
                .orElse(null);
        List<String> permissions = userLoginDetails.getUser().roles().stream()
                .filter(p -> !p.startsWith("ROLE_"))
                .toList();
        return buildLoginResponse(userLoginDetails.getUser(), role, permissions);
    }

    private LoginResponse buildLoginResponse(User user, String token) {
        return new LoginResponse(
                user.getUserId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getIsActive(),
                user.getAvatarUrl(),
                token,
                null,
                null
        );
    }

    private LoginResponse buildLoginResponse(UserLogged user, String role, List<String> permissions) {
        return new LoginResponse(
                user.userId(),
                user.email(),
                user.displayName(),
                user.isActive(),
                user.avatarUrl(),
                null,
                role,
                permissions
        );
    }

}

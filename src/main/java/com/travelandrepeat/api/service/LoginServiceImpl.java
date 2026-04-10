package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLogged;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.travelandrepeat.api.dto.Role.*;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${env.local}")
    private boolean isLocal;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    public String login(LoginRequest loginRequest, HttpServletResponse response) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User userResult = userService.getUserByEmail(loginRequest.email());

        if (userResult == null) return "USER_NOT_FOUND"; // user does not exist

        if (!userResult.getIsActive()) return "ACCOUNT_DISABLED"; // account disabled

        if (!passwordEncoder.matches(loginRequest.password(), userResult.getHashedPassword()))
            return "INVALID_PASSWORD"; // invalid password

        String token = jwtService.generateToken(userResult);
        cookieOperations(token, response, true);

        return "Login Success: " + userResult.getUserId() + " -> " + userResult.getDisplayName();
    }

    private void cookieOperations(String token, HttpServletResponse response, boolean isCreate) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(!isLocal)// localhost → false
                .path("/")
                .maxAge(isCreate ? 60 * 60 : 0)
                .sameSite(isLocal ? "Lax" : "None")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
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

    @Override
    public String logout(HttpServletResponse response) {
        cookieOperations("", response, false);
        return "Successfully user deauthenticated!";
    }

    private LoginResponse buildLoginResponse(UserLogged user, String role, List<String> permissions) {
        return new LoginResponse(
                user.userId(),
                user.email(),
                user.displayName(),
                user.isActive(),
                user.avatarUrl(),
                role,
                permissions
        );
    }

}

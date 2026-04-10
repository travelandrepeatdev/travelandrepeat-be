package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLoginDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface LoginService {
    String login(LoginRequest loginRequest, HttpServletResponse response);
    LoginResponse profile(UserLoginDetails userLoginDetails);
    String logout(HttpServletResponse response);
}

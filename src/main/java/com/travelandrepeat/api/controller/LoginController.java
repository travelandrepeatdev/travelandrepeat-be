package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest credentials, HttpServletResponse response) {
        String loginResponse = loginService.login(credentials, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        String result = loginService.logout(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/profile")
    public LoginResponse profile(@AuthenticationPrincipal UserLoginDetails userLoginDetails) {
        return loginService.profile(userLoginDetails);
    }
}

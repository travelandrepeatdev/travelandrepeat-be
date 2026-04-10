package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest credentials, HttpServletResponse response) {
        String loginResponse = loginService.login(credentials, response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        String result = loginService.logout(response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile")
    public ResponseEntity<LoginResponse> profile(@AuthenticationPrincipal UserLoginDetails userLoginDetails) {
        LoginResponse loginResponse = loginService.profile(userLoginDetails);
        return ResponseEntity.ok(loginResponse);
    }
}

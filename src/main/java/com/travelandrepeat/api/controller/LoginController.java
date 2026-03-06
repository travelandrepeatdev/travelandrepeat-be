package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest credentials) {
        LoginResponse loginResponse = loginService.login(credentials);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal UserLoginDetails userLoginDetails) {
        LoginResponse loginResponse = loginService.profile(userLoginDetails);
        return ResponseEntity.ok(loginResponse);
    }
}

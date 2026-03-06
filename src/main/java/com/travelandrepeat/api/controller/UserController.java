package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.dto.UserResponse;
import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.entity.UserRole;
import com.travelandrepeat.api.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/userList")
    public ResponseEntity<List<UserResponse>> getUserList() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/userRoleList")
    public ResponseEntity<List<UserRole>> getUserRoleList() {
        return ResponseEntity.ok(userService.getAllUserRoles());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/userRole")
    public ResponseEntity<UserRole> addUserRole(@RequestBody UserRole userRole) {
        return ResponseEntity.ok(userService.addUserRoles(userRole));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/userRole")
    public ResponseEntity<UserRole> updateUserRole(@RequestBody UserRole userRole) {
        return ResponseEntity.ok(userService.updateUserRoles(userRole));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/user")
    public ResponseEntity<UserResponse> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/userEnableDisable")
    public ResponseEntity<UserResponse> userEnableDisable(@PathParam("userId") UUID userId) {
        return ResponseEntity.ok(userService.enableDisableUser(userId));
    }
}

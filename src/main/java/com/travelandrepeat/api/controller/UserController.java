package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.UserResponse;
import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.entity.UserRole;
import com.travelandrepeat.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getUserList() {
        return userService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/userRole")
    public List<UserRole> getUserRoleList() {
        return userService.getAllUserRoles();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/userRole")
    public ResponseEntity<UserRole> addUserRole(@RequestBody UserRole userRole) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUserRoles(userRole));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/userRole")
    public ResponseEntity<UserRole> updateUserRole(@RequestBody UserRole userRole) {
        return ResponseEntity.ok(userService.updateUserRoles(userRole));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}/enable-disable")
    public ResponseEntity<UserResponse> userEnableDisable(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.enableDisableUser(id));
    }
}

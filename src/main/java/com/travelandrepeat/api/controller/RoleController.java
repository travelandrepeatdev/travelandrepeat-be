package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Role> getRoleList() {
        return roleService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/rolePermission")
    public List<RolePermission> getRolePermissionList() {
        return roleService.getAllRolePermissions();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UUID> deleteRole(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/rolePermission")
    public ResponseEntity<List<RolePermission>> createPermission(@RequestBody List<RolePermission> rolePermissionList) {
        return ResponseEntity.ok(roleService.addRolePermissionList(rolePermissionList));
    }
}

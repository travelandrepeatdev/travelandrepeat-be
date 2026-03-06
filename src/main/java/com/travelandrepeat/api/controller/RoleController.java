package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/roleList")
    public List<Role> getRoleList() {
        return roleService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/rolePermissionList")
    public List<RolePermission> getRolePermissionList() {
        return roleService.getAllRolePermissions();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/role")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/role")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/role")
    public ResponseEntity<UUID> deleteRole(@RequestParam UUID roleId) {
        return ResponseEntity.ok(roleService.deleteRole(roleId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/rolePermissionList")
    public ResponseEntity<List<RolePermission>> createPermission(@RequestBody List<RolePermission> rolePermissionList) {
        return ResponseEntity.ok(roleService.addRolePermissionList(rolePermissionList));
    }
}

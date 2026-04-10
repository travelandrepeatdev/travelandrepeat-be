package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Permission> getPermissionList() {
        return permissionService.getPermissionList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Permission addPermission(@RequestBody Permission permission) {
        return permissionService.addPermission(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public Permission updatePermission(@RequestBody Permission permission) {
        return permissionService.updatePermission(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UUID> deleteProvider(@PathVariable UUID id) {
        return ResponseEntity.ok(permissionService.removePermission(id));
    }
}

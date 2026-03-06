package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.service.PermissionService;
import jakarta.websocket.server.PathParam;
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
    @GetMapping(path = "/permissionList")
    public List<Permission> getPermissionList() {
        return permissionService.getPermissionList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/permission")
    public Permission addPermission(@RequestBody Permission permission) {
        return permissionService.addPermission(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/permission")
    public Permission updatePermission(@RequestBody Permission permission) {
        return permissionService.updatePermission(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/permission")
    public ResponseEntity<UUID> deleteProvider(@PathParam("permissionId") UUID permissionId) {
        return ResponseEntity.ok(permissionService.removePermission(permissionId));
    }
}

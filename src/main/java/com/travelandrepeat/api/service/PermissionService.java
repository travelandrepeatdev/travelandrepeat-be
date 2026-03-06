package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Permission;

import java.util.List;
import java.util.UUID;

public interface PermissionService {
    List<Permission> getPermissionList();
    Permission addPermission(Permission permission);
    Permission updatePermission(Permission permission);
    UUID removePermission(UUID permissionId);
}

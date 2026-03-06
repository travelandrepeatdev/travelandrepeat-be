package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.entity.RolePermissionId;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    List<Role> getAll();
    List<RolePermission> getAllRolePermissions();
    Role createRole(Role role);
    Role updateRole(Role role);
    UUID deleteRole(UUID roleId);
    List<RolePermission> addRolePermissionList(List<RolePermission> rolePermissionList);
}

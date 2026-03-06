package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.repository.PermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;

    @Override
    public List<Permission> getPermissionList() {
        return permissionRepo.findAll();
    }

    @Override
    public Permission addPermission(Permission permission) {
        permission.setPermissionId(null);
        permission.setCreatedAt(LocalDateTime.now());
        return permissionRepo.save(permission);
    }

    @Override
    public Permission updatePermission(Permission permission) {
        Permission currentPermission = permissionRepo.findById(permission.getPermissionId()).orElse(null);
        if (currentPermission != null) {
            currentPermission.setDescription(permission.getDescription());
            currentPermission.setName(permission.getName());
            return permissionRepo.save(currentPermission);
        }
        return null;
    }

    @Override
    public UUID removePermission(UUID permissionId) {
        Permission currentPermission = permissionRepo.findById(permissionId).orElse(null);
        if (currentPermission != null) {
            permissionRepo.deleteById(currentPermission.getPermissionId());
            return permissionId;
        }
        return null;
    }
}

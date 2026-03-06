package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.entity.RolePermissionId;
import com.travelandrepeat.api.entity.UserRole;
import com.travelandrepeat.api.repository.RolePermissionRepo;
import com.travelandrepeat.api.repository.RoleRepo;
import com.travelandrepeat.api.repository.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private RolePermissionRepo rolePermissionRepo;

    @Override
    public List<Role> getAll() {
        return roleRepo.findAll();
    }

    @Override
    public List<RolePermission> getAllRolePermissions() {
        return rolePermissionRepo.findAll();
    }

    @Override
    public Role createRole(Role role) {
        role.setCreatedAt(LocalDateTime.now());
        return roleRepo.save(role);
    }

    @Override
    public Role updateRole(Role role) {
        Role roleCurrent = roleRepo.findById(role.getRoleId()).orElse(null);
        if (roleCurrent != null) {
            roleCurrent.setName(role.getName());
            roleCurrent.setDescription(role.getDescription());
            return roleRepo.save(roleCurrent);
        }
        return null;
    }

    @Override
    public UUID deleteRole(UUID roleId) {
        Role roleIdToDelete = roleRepo.findById(roleId).orElse(null);
        if (roleIdToDelete != null) {
            roleRepo.deleteById(roleIdToDelete.getRoleId());
            return roleIdToDelete.getRoleId();
        }
        return null;
    }

    @Transactional
    @Override
    public List<RolePermission> addRolePermissionList(List<RolePermission> rolePermissionList) {
        if (rolePermissionList != null && !rolePermissionList.isEmpty()) {
            rolePermissionRepo.deleteByRoleId(rolePermissionList.get(0).getRoleId());

            if (rolePermissionList.get(0).getPermissionId() != null) {
                rolePermissionList.forEach(rolePermission -> rolePermissionRepo.save(rolePermission));
                return rolePermissionList;
            }
        }
        return List.of();
    }

}

package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepo extends JpaRepository<Permission, UUID> {
}

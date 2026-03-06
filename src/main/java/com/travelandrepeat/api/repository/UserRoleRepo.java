package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepo extends JpaRepository<UserRole, UUID> {
}

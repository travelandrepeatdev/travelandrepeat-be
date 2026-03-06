package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
}

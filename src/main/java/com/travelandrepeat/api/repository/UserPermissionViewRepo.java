package com.travelandrepeat.api.repository;

import com.travelandrepeat.api.entity.UserPermissionView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionViewRepo extends JpaRepository<UserPermissionView, String> {
}

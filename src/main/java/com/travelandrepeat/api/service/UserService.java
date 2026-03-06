package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.UserResponse;
import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.entity.UserRole;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserByEmail(String email);
    List<UserResponse> getAll();
    List<UserRole> getAllUserRoles();
    UserResponse addUser(User user);
    UserResponse updateUser(User user);
    UserRole addUserRoles(UserRole userRole);
    UserRole updateUserRoles(UserRole userRole);
    UserResponse enableDisableUser(UUID userId);
}

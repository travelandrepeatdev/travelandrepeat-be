package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.UserResponse;
import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.entity.UserRole;
import com.travelandrepeat.api.repository.UserRepo;
import com.travelandrepeat.api.repository.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserRoleRepo userRoleRepo;

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> userList = userRepo.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        userList.forEach(user -> userResponseList.add(
                UserResponse.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .displayName(user.getDisplayName())
                        .avatarUrl(user.getAvatarUrl())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .lastLogin(user.getLastLogin())
                        .isActive(user.getIsActive())
                        .role(extractRoleFromUser(user))
                        .build()
        ));
        return userResponseList;
    }

    private String extractRoleFromUser(User user) {
        String role = "";
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            role = user.getRoles().get(0).getName();
        }
        return role;
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepo.findAll();
    }

    @Override
    public UserResponse addUser(User user) {
        user.setUserId(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setHashedPassword("no password yet");
        User entitySaved = userRepo.save(user);
        return UserResponse.builder()
                .userId(entitySaved.getUserId())
                .email(entitySaved.getEmail())
                .displayName(entitySaved.getDisplayName())
                .avatarUrl(entitySaved.getAvatarUrl())
                .createdAt(entitySaved.getCreatedAt())
                .updatedAt(entitySaved.getUpdatedAt())
                .lastLogin(entitySaved.getLastLogin())
                .isActive(entitySaved.getIsActive())
                .role(extractRoleFromUser(entitySaved))
                .build();
    }

    @Override
    public UserResponse updateUser(User user) {
        User currentUser = userRepo.findById(user.getUserId()).orElse(null);
        if (currentUser != null) {
            currentUser.setDisplayName(user.getDisplayName());
            currentUser.setUpdatedAt(LocalDateTime.now());
            currentUser.setEmail(user.getEmail());
            User userSaved = userRepo.save(currentUser);

            return UserResponse.builder()
                    .userId(userSaved.getUserId())
                    .email(userSaved.getEmail())
                    .displayName(userSaved.getDisplayName())
                    .avatarUrl(userSaved.getAvatarUrl())
                    .createdAt(userSaved.getCreatedAt())
                    .updatedAt(userSaved.getUpdatedAt())
                    .lastLogin(userSaved.getLastLogin())
                    .isActive(userSaved.getIsActive())
                    .role(extractRoleFromUser(userSaved))
                    .build();
        }
        return null;
    }

    @Override
    public UserRole addUserRoles(UserRole userRole) {
        userRole.setAssignedAt(LocalDateTime.now());
        return userRoleRepo.save(userRole);
    }

    @Override
    public UserRole updateUserRoles(UserRole userRole) {
        UserRole currentUserRole = userRoleRepo.findById(userRole.getUserId()).orElse(null);
        if (currentUserRole != null) {
            currentUserRole.setAssignedAt(LocalDateTime.now());
            currentUserRole.setRoleId(userRole.getRoleId());
            return userRoleRepo.save(currentUserRole);
        }
        userRole.setAssignedAt(LocalDateTime.now());
        return userRoleRepo.save(userRole);
    }

    @Override
    public UserResponse enableDisableUser(UUID userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            user.setIsActive(!user.getIsActive());
            user.setUpdatedAt(LocalDateTime.now());
            User userSaved = userRepo.save(user);
            return UserResponse.builder()
                    .userId(userSaved.getUserId())
                    .email(userSaved.getEmail())
                    .displayName(userSaved.getDisplayName())
                    .avatarUrl(userSaved.getAvatarUrl())
                    .createdAt(userSaved.getCreatedAt())
                    .updatedAt(userSaved.getUpdatedAt())
                    .lastLogin(userSaved.getLastLogin())
                    .isActive(userSaved.getIsActive())
                    .role(extractRoleFromUser(userSaved))
                    .build();
        }
        return null;
    }
}

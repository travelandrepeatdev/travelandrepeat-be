package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.UserResponse;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.entity.UserRole;
import com.travelandrepeat.api.repository.UserRepo;
import com.travelandrepeat.api.repository.UserRoleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserRoleRepo userRoleRepo;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID testUserId;
    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testRole = new Role();
        testRole.setRoleId(UUID.randomUUID());
        testRole.setName("ADMIN");

        testUser = new User();
        testUser.setUserId(testUserId);
        testUser.setEmail("test@example.com");
        testUser.setDisplayName("Test User");
        testUser.setHashedPassword("hashedPassword123");
        testUser.setIsActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser.setRoles(List.of(testRole));
    }

    @Nested
    @DisplayName("getUserByEmail Tests")
    class GetUserByEmailTests {

        @Test
        @DisplayName("Should return user when email exists")
        void shouldReturnUserWhenEmailExists() {
            // Arrange
            when(userRepo.findByEmail("test@example.com")).thenReturn(testUser);

            // Act
            User result = userService.getUserByEmail("test@example.com");

            // Assert
            assertNotNull(result);
            assertEquals("test@example.com", result.getEmail());
            assertEquals("Test User", result.getDisplayName());
            verify(userRepo, times(1)).findByEmail("test@example.com");
        }

        @Test
        @DisplayName("Should return null when email does not exist")
        void shouldReturnNullWhenEmailDoesNotExist() {
            // Arrange
            when(userRepo.findByEmail("nonexistent@example.com")).thenReturn(null);

            // Act
            User result = userService.getUserByEmail("nonexistent@example.com");

            // Assert
            assertNull(result);
            verify(userRepo, times(1)).findByEmail("nonexistent@example.com");
        }

        @Test
        @DisplayName("Should handle empty string email")
        void shouldHandleEmptyStringEmail() {
            // Arrange
            when(userRepo.findByEmail("")).thenReturn(null);

            // Act
            User result = userService.getUserByEmail("");

            // Assert
            assertNull(result);
            verify(userRepo, times(1)).findByEmail("");
        }
    }

    @Nested
    @DisplayName("getAll Tests")
    class GetAllTests {

        @Test
        @DisplayName("Should return list of all users as UserResponse")
        void shouldReturnListOfAllUsers() {
            // Arrange
            User user2 = new User();
            user2.setUserId(UUID.randomUUID());
            user2.setEmail("user2@example.com");
            user2.setDisplayName("User Two");
            user2.setRoles(List.of(testRole));
            user2.setCreatedAt(LocalDateTime.now());
            user2.setUpdatedAt(LocalDateTime.now());

            List<User> userList = List.of(testUser, user2);
            when(userRepo.findAll()).thenReturn(userList);

            // Act
            List<UserResponse> result = userService.getAll();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("test@example.com", result.get(0).email());
            assertEquals("user2@example.com", result.get(1).email());
            verify(userRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyListWhenNoUsersExist() {
            // Arrange
            when(userRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<UserResponse> result = userService.getAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should handle user with no roles")
        void shouldHandleUserWithNoRoles() {
            // Arrange
            testUser.setRoles(new ArrayList<>());
            when(userRepo.findAll()).thenReturn(List.of(testUser));

            // Act
            List<UserResponse> result = userService.getAll();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("", result.get(0).role());
            verify(userRepo, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getAllUserRoles Tests")
    class GetAllUserRolesTests {

        @Test
        @DisplayName("Should return all user roles")
        void shouldReturnAllUserRoles() {
            // Arrange
            UserRole userRole = new UserRole();
            userRole.setUserId(testUserId);
            userRole.setRoleId(UUID.randomUUID());

            List<UserRole> userRoleList = List.of(userRole);
            when(userRoleRepo.findAll()).thenReturn(userRoleList);

            // Act
            List<UserRole> result = userService.getAllUserRoles();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(testUserId, result.get(0).getUserId());
            verify(userRoleRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no user roles exist")
        void shouldReturnEmptyListWhenNoUserRolesExist() {
            // Arrange
            when(userRoleRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<UserRole> result = userService.getAllUserRoles();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(userRoleRepo, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("addUser Tests")
    class AddUserTests {

        @Test
        @DisplayName("Should create new user successfully")
        void shouldCreateNewUserSuccessfully() {
            // Arrange
            User newUser = new User();
            newUser.setEmail("newuser@example.com");
            newUser.setDisplayName("New User");
            newUser.setIsActive(true);
            newUser.setRoles(List.of(testRole));

            User savedUser = new User();
            savedUser.setUserId(UUID.randomUUID());
            savedUser.setEmail("newuser@example.com");
            savedUser.setDisplayName("New User");
            savedUser.setIsActive(true);
            savedUser.setCreatedAt(LocalDateTime.now());
            savedUser.setUpdatedAt(LocalDateTime.now());
            savedUser.setHashedPassword("no password yet");
            savedUser.setRoles(List.of(testRole));

            when(userRepo.save(any(User.class))).thenReturn(savedUser);

            // Act
            UserResponse result = userService.addUser(newUser);

            // Assert
            assertNotNull(result);
            assertEquals("newuser@example.com", result.email());
            assertEquals("New User", result.displayName());
            assertEquals("ADMIN", result.role());
            verify(userRepo, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Should set userId to null when adding new user")
        void shouldSetUserIdToNullWhenAddingNewUser() {
            // Arrange
            User newUser = new User();
            newUser.setUserId(UUID.randomUUID());
            newUser.setEmail("newuser@example.com");
            newUser.setDisplayName("New User");
            newUser.setRoles(List.of(testRole));

            User savedUser = new User();
            savedUser.setUserId(UUID.randomUUID());
            savedUser.setEmail("newuser@example.com");
            savedUser.setDisplayName("New User");
            savedUser.setCreatedAt(LocalDateTime.now());
            savedUser.setUpdatedAt(LocalDateTime.now());
            savedUser.setHashedPassword("no password yet");
            savedUser.setRoles(List.of(testRole));

            when(userRepo.save(any(User.class))).thenReturn(savedUser);

            // Act
            userService.addUser(newUser);

            // Assert
            verify(userRepo, times(1)).save(argThat(user -> user.getUserId() == null));
        }

        @Test
        @DisplayName("Should set timestamps when adding user")
        void shouldSetTimestampsWhenAddingUser() {
            // Arrange
            User newUser = new User();
            newUser.setEmail("newuser@example.com");
            newUser.setRoles(List.of(testRole));

            User savedUser = new User();
            savedUser.setUserId(UUID.randomUUID());
            savedUser.setEmail("newuser@example.com");
            savedUser.setCreatedAt(LocalDateTime.now());
            savedUser.setUpdatedAt(LocalDateTime.now());
            savedUser.setHashedPassword("no password yet");
            savedUser.setRoles(List.of(testRole));

            when(userRepo.save(any(User.class))).thenReturn(savedUser);

            // Act
            UserResponse result = userService.addUser(newUser);

            // Assert
            assertNotNull(result.createdAt());
            assertNotNull(result.updatedAt());
            verify(userRepo, times(1)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("updateUser Tests")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update existing user successfully")
        void shouldUpdateExistingUserSuccessfully() {
            // Arrange
            User updatedUser = new User();
            updatedUser.setUserId(testUserId);
            updatedUser.setEmail("updated@example.com");
            updatedUser.setDisplayName("Updated User");
            updatedUser.setCreatedAt(testUser.getCreatedAt());
            updatedUser.setUpdatedAt(LocalDateTime.now());
            updatedUser.setRoles(List.of(testRole));

            when(userRepo.findById(updatedUser.getUserId())).thenReturn(Optional.of(updatedUser));
            when(userRepo.save(any(User.class))).thenReturn(updatedUser);

            // Act
            UserResponse result = userService.updateUser(updatedUser);

            // Assert
            assertNotNull(result);
            assertEquals("updated@example.com", result.email());
            assertEquals("Updated User", result.displayName());
            verify(userRepo, times(1)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("addUserRoles Tests")
    class AddUserRolesTests {

        @Test
        @DisplayName("Should add user role successfully")
        void shouldAddUserRoleSuccessfully() {
            // Arrange
            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(testUserId);
            newUserRole.setRoleId(UUID.randomUUID());

            UserRole savedUserRole = new UserRole();
            savedUserRole.setUserId(testUserId);
            savedUserRole.setRoleId(UUID.randomUUID());

            when(userRoleRepo.save(any(UserRole.class))).thenReturn(savedUserRole);

            // Act
            UserRole result = userService.addUserRoles(newUserRole);

            // Assert
            assertNotNull(result);
            assertEquals(testUserId, result.getUserId());
            verify(userRoleRepo, times(1)).save(any(UserRole.class));
        }
    }

    @Nested
    @DisplayName("updateUserRoles Tests")
    class UpdateUserRolesTests {

        @Test
        @DisplayName("Should update user role successfully")
        void shouldUpdateUserRoleSuccessfully() {
            // Arrange
            UserRole updatedUserRole = new UserRole();
            updatedUserRole.setUserId(testUserId);
            updatedUserRole.setRoleId(UUID.randomUUID());

            when(userRoleRepo.save(any(UserRole.class))).thenReturn(updatedUserRole);

            // Act
            UserRole result = userService.updateUserRoles(updatedUserRole);

            // Assert
            assertNotNull(result);
            assertEquals(testUserId, result.getUserId());
            verify(userRoleRepo, times(1)).save(any(UserRole.class));
        }
    }

    @Nested
    @DisplayName("enableDisableUser Tests")
    class EnableDisableUserTests {

        @Test
        @DisplayName("Should toggle user active status successfully")
        void shouldToggleUserActiveStatusSuccessfully() {
            // Arrange
            testUser.setIsActive(true);
            when(userRepo.findById(testUserId)).thenReturn(java.util.Optional.of(testUser));
            when(userRepo.save(any(User.class))).thenReturn(testUser);

            // Act
            UserResponse result = userService.enableDisableUser(testUserId);

            // Assert
            assertNotNull(result);
            verify(userRepo, times(1)).findById(testUserId);
            verify(userRepo, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Should return null when user not found")
        void shouldReturnNullWhenUserNotFound() {
            // Arrange
            when(userRepo.findById(testUserId)).thenReturn(java.util.Optional.empty());

            // Act
            UserResponse result = userService.enableDisableUser(testUserId);

            // Assert
            assertNull(result);
            verify(userRepo, times(1)).findById(testUserId);
            verify(userRepo, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should deactivate user when active is true")
        void shouldDeactivateUserWhenActiveIsTrue() {
            // Arrange
            testUser.setIsActive(true);
            when(userRepo.findById(testUserId)).thenReturn(java.util.Optional.of(testUser));
            when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
                return invocation.<User>getArgument(0);
            });

            // Act
            userService.enableDisableUser(testUserId);

            // Assert
            verify(userRepo).save(argThat(user -> !user.getIsActive()));
        }
    }
}


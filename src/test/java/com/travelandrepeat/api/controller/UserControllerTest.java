package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.UserResponse;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.entity.UserRole;
import com.travelandrepeat.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private UUID testUserId;
    private User testUser;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        testUserId = UUID.randomUUID();

        Role testRole = new Role();
        testRole.setRoleId(UUID.randomUUID());
        testRole.setName("ADMIN");

        testUser = new User();
        testUser.setUserId(testUserId);
        testUser.setEmail("admin@example.com");
        testUser.setDisplayName("Admin User");
        testUser.setIsActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser.setRoles(List.of(testRole));

        testUserResponse = UserResponse.builder()
                .userId(testUserId)
                .email("admin@example.com")
                .displayName("Admin User")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role("ADMIN")
                .build();
    }

    @Nested
    @DisplayName("getUserList Tests")
    class GetUserListTests {

        @Test
        @DisplayName("Should return list of all users")
        void shouldReturnListOfAllUsers() {
            // Arrange
            List<UserResponse> userList = List.of(testUserResponse);
            when(userService.getAll()).thenReturn(userList);

            // Act
            ResponseEntity<List<UserResponse>> response = userController.getUserList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("admin@example.com", response.getBody().get(0).email());
            verify(userService, times(1)).getAll();
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyListWhenNoUsersExist() {
            // Arrange
            when(userService.getAll()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<UserResponse>> response = userController.getUserList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(userService, times(1)).getAll();
        }
    }

    @Nested
    @DisplayName("getUserRoleList Tests")
    class GetUserRoleListTests {

        @Test
        @DisplayName("Should return list of all user roles")
        void shouldReturnListOfAllUserRoles() {
            // Arrange
            UserRole userRole = new UserRole();
            userRole.setUserId(testUserId);
            userRole.setRoleId(UUID.randomUUID());

            List<UserRole> userRoleList = List.of(userRole);
            when(userService.getAllUserRoles()).thenReturn(userRoleList);

            // Act
            ResponseEntity<List<UserRole>> response = userController.getUserRoleList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            verify(userService, times(1)).getAllUserRoles();
        }

        @Test
        @DisplayName("Should return empty list when no user roles exist")
        void shouldReturnEmptyListWhenNoUserRolesExist() {
            // Arrange
            when(userService.getAllUserRoles()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<UserRole>> response = userController.getUserRoleList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(userService, times(1)).getAllUserRoles();
        }
    }

    @Nested
    @DisplayName("addUserRole Tests")
    class AddUserRoleTests {

        @Test
        @DisplayName("Should add user role successfully")
        void shouldAddUserRoleSuccessfully() {
            // Arrange
            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(testUserId);
            newUserRole.setRoleId(UUID.randomUUID());

            when(userService.addUserRoles(any(UserRole.class))).thenReturn(newUserRole);

            // Act
            ResponseEntity<UserRole> response = userController.addUserRole(newUserRole);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(testUserId, response.getBody().getUserId());
            verify(userService, times(1)).addUserRoles(any(UserRole.class));
        }
    }

    @Nested
    @DisplayName("updateUserRole Tests")
    class UpdateUserRoleTests {

        @Test
        @DisplayName("Should update user role successfully")
        void shouldUpdateUserRoleSuccessfully() {
            // Arrange
            UserRole updatedUserRole = new UserRole();
            updatedUserRole.setUserId(testUserId);
            updatedUserRole.setRoleId(UUID.randomUUID());

            when(userService.updateUserRoles(any(UserRole.class))).thenReturn(updatedUserRole);

            // Act
            ResponseEntity<UserRole> response = userController.updateUserRole(updatedUserRole);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(testUserId, response.getBody().getUserId());
            verify(userService, times(1)).updateUserRoles(any(UserRole.class));
        }
    }

    @Nested
    @DisplayName("addUser Tests")
    class AddUserTests {

        @Test
        @DisplayName("Should add new user successfully")
        void shouldAddNewUserSuccessfully() {
            // Arrange
            User newUser = new User();
            newUser.setEmail("newuser@example.com");
            newUser.setDisplayName("New User");

            when(userService.addUser(any(User.class))).thenReturn(testUserResponse);

            // Act
            ResponseEntity<UserResponse> response = userController.addUser(newUser);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("admin@example.com", response.getBody().email());
            verify(userService, times(1)).addUser(any(User.class));
        }

        @Test
        @DisplayName("Should return response entity with user response")
        void shouldReturnResponseEntityWithUserResponse() {
            // Arrange
            User newUser = new User();
            when(userService.addUser(any(User.class))).thenReturn(testUserResponse);

            // Act
            ResponseEntity<UserResponse> response = userController.addUser(newUser);

            // Assert
            assertNotNull(response);
            assertNotNull(response.getBody());
            assertTrue(response.getStatusCode().is2xxSuccessful());
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

            UserResponse updatedResponse = UserResponse.builder()
                    .userId(testUserId)
                    .email("updated@example.com")
                    .displayName("Updated User")
                    .build();

            when(userService.updateUser(any(User.class))).thenReturn(updatedResponse);

            // Act
            ResponseEntity<UserResponse> response = userController.updateUser(updatedUser);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("updated@example.com", response.getBody().email());
            verify(userService, times(1)).updateUser(any(User.class));
        }
    }

    @Nested
    @DisplayName("userEnableDisable Tests")
    class UserEnableDisableTests {

        @Test
        @DisplayName("Should toggle user enable disable successfully")
        void shouldToggleUserEnableDisableSuccessfully() {
            // Arrange
            when(userService.enableDisableUser(testUserId)).thenReturn(testUserResponse);

            // Act
            ResponseEntity<UserResponse> response = userController.userEnableDisable(testUserId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(userService, times(1)).enableDisableUser(testUserId);
        }

        @Test
        @DisplayName("Should handle non-existent user id")
        void shouldHandleNonExistentUserId() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(userService.enableDisableUser(nonExistentId)).thenReturn(null);

            // Act
            ResponseEntity<UserResponse> response = userController.userEnableDisable(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
            verify(userService, times(1)).enableDisableUser(nonExistentId);
        }

        @Test
        @DisplayName("Should return response with updated user status")
        void shouldReturnResponseWithUpdatedUserStatus() {
            // Arrange
            UserResponse disabledUserResponse = UserResponse.builder()
                    .userId(testUserId)
                    .email("admin@example.com")
                    .displayName("Admin User")
                    .isActive(false)
                    .build();

            when(userService.enableDisableUser(testUserId)).thenReturn(disabledUserResponse);

            // Act
            ResponseEntity<UserResponse> response = userController.userEnableDisable(testUserId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isActive());
            verify(userService, times(1)).enableDisableUser(testUserId);
        }
    }
}


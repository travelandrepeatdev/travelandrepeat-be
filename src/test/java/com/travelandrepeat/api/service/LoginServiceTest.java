package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLogged;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService Tests")
class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginServiceImpl loginService;

    private UUID testUserId;
    private User testUser;
    private Role testRole;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        passwordEncoder = new BCryptPasswordEncoder();

        testRole = new Role();
        testRole.setRoleId(UUID.randomUUID());
        testRole.setName("ADMIN");

        testUser = new User();
        testUser.setUserId(testUserId);
        testUser.setEmail("admin@example.com");
        testUser.setDisplayName("Admin User");
        testUser.setHashedPassword(passwordEncoder.encode("password123"));
        testUser.setIsActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser.setRoles(List.of(testRole));
    }

    @Nested
    @DisplayName("login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with correct credentials")
        void shouldLoginSuccessfullyWithCorrectCredentials() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "password123");
            when(userService.getUserByEmail("admin@example.com")).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("jwt-token-123");

            // Act
            LoginResponse result = loginService.login(loginRequest);

            // Assert
            assertNotNull(result);
            assertEquals("admin@example.com", result.email());
            assertEquals("Admin User", result.name());
            assertEquals("jwt-token-123", result.accessToken());
            assertTrue(result.isActive());
            verify(userService, times(1)).getUserByEmail("admin@example.com");
            verify(jwtService, times(1)).generateToken(testUser);
        }

        @Test
        @DisplayName("Should return null when user email not found")
        void shouldReturnNullWhenUserEmailNotFound() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "password123");
            when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(null);

            // Act
            LoginResponse result = loginService.login(loginRequest);

            // Assert
            assertNull(result);
            verify(userService, times(1)).getUserByEmail("nonexistent@example.com");
            verify(jwtService, never()).generateToken(any());
        }

        @Test
        @DisplayName("Should return response without token when password is incorrect")
        void shouldReturnResponseWithoutTokenWhenPasswordIncorrect() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "wrongpassword");
            when(userService.getUserByEmail("admin@example.com")).thenReturn(testUser);

            // Act
            LoginResponse result = loginService.login(loginRequest);

            // Assert
            assertNotNull(result);
            assertEquals("admin@example.com", result.email());
            assertNull(result.accessToken());
            verify(userService, times(1)).getUserByEmail("admin@example.com");
            verify(jwtService, never()).generateToken(any());
        }

        @Test
        @DisplayName("Should return null when user account is inactive")
        void shouldHandleInactiveUserAccount() {
            // Arrange
            testUser.setIsActive(false);
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "password123");
            when(userService.getUserByEmail("admin@example.com")).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("jwt-token-123");

            // Act
            LoginResponse result = loginService.login(loginRequest);

            // Assert
            assertNotNull(result);
            assertFalse(result.isActive());
            verify(userService, times(1)).getUserByEmail("admin@example.com");
        }

        @Test
        @DisplayName("Should handle empty email")
        void shouldHandleEmptyEmail() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("", "password123");
            when(userService.getUserByEmail("")).thenReturn(null);

            // Act
            LoginResponse result = loginService.login(loginRequest);

            // Assert
            assertNull(result);
            verify(userService, times(1)).getUserByEmail("");
        }

        @Test
        @DisplayName("Should handle empty password")
        void shouldHandleEmptyPassword() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "");
            when(userService.getUserByEmail("admin@example.com")).thenReturn(testUser);

            // Act
            LoginResponse result = loginService.login(loginRequest);

            // Assert
            assertNotNull(result);
            assertNull(result.accessToken());
            verify(userService, times(1)).getUserByEmail("admin@example.com");
        }
    }

    @Nested
    @DisplayName("profile Tests")
    class ProfileTests {

        @Test
        @DisplayName("Should return profile with role and permissions")
        void shouldReturnProfileWithRoleAndPermissions() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    "avatar-url",
                    true,
                    List.of("ROLE_ADMIN", "ADMIN", "USER_READ", "USER_WRITE")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            // Act
            LoginResponse result = loginService.profile(userLoginDetails);

            // Assert
            assertNotNull(result);
            assertEquals("admin@example.com", result.email());
            assertEquals("Admin User", result.name());
            assertEquals("ADMIN", result.role());
            assertTrue(result.permissions().contains("USER_READ"));
            assertTrue(result.permissions().contains("USER_WRITE"));
            assertNull(result.accessToken());
        }

        @Test
        @DisplayName("Should return null when UserLoginDetails is null")
        void shouldReturnNullWhenUserLoginDetailsIsNull() {
            // Act
            LoginResponse result = loginService.profile(null);

            // Assert
            assertNull(result);
        }

        @Test
        @DisplayName("Should extract ADMIN role from roles list")
        void shouldExtractAdminRoleFromRolesList() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    null,
                    true,
                    List.of("ROLE_ADMIN", "ADMIN")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            // Act
            LoginResponse result = loginService.profile(userLoginDetails);

            // Assert
            assertNotNull(result);
            assertEquals("ADMIN", result.role());
        }

        @Test
        @DisplayName("Should extract AGENT role from roles list")
        void shouldExtractAgentRoleFromRolesList() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "agent@example.com",
                    "Agent User",
                    null,
                    true,
                    List.of("ROLE_AGENT", "AGENT")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            // Act
            LoginResponse result = loginService.profile(userLoginDetails);

            // Assert
            assertNotNull(result);
            assertEquals("AGENT", result.role());
        }

        @Test
        @DisplayName("Should filter out ROLE_ prefixed permissions")
        void shouldFilterOutRolePrefixedPermissions() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "user@example.com",
                    "Test User",
                    null,
                    true,
                    List.of("ROLE_USER", "USER", "CLIENT_READ", "CLIENT_WRITE")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            // Act
            LoginResponse result = loginService.profile(userLoginDetails);

            // Assert
            assertNotNull(result);
            assertFalse(result.permissions().contains("ROLE_USER"));
            assertTrue(result.permissions().contains("CLIENT_READ"));
            assertTrue(result.permissions().contains("CLIENT_WRITE"));
        }

        @Test
        @DisplayName("Should return empty permissions list when no permissions exist")
        void shouldReturnEmptyPermissionsListWhenNoPermissionsExist() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "user@example.com",
                    "Test User",
                    null,
                    true,
                    List.of("ROLE_USER")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            // Act
            LoginResponse result = loginService.profile(userLoginDetails);

            // Assert
            assertNotNull(result);
            assertTrue(result.permissions().isEmpty());
        }
    }
}


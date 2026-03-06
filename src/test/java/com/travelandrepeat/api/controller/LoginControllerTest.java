package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLogged;
import com.travelandrepeat.api.dto.UserLoginDetails;
import com.travelandrepeat.api.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginController Tests")
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private LoginController loginController;

    private UUID testUserId;
    private LoginResponse testLoginResponse;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        testLoginResponse = new LoginResponse(
                testUserId,
                "admin@example.com",
                "Admin User",
                true,
                "avatar-url",
                "jwt-token-123",
                "ADMIN",
                List.of("USER_READ", "USER_WRITE")
        );
    }

    @Nested
    @DisplayName("login Tests")
    class LoginTests {

        //@Test
        @DisplayName("Should login successfully with correct credentials")
        void shouldLoginSuccessfullyWithCorrectCredentials() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "password123");
            when(loginService.login(loginRequest)).thenReturn(testLoginResponse);

            // Act
            ResponseEntity<?> response = loginController.login(loginRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(loginService, times(1)).login(loginRequest);
        }

        @Test
        @DisplayName("Should return login response with token")
        void shouldReturnLoginResponseWithToken() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "password123");
            when(loginService.login(loginRequest)).thenReturn(testLoginResponse);

            // Act
            ResponseEntity<?> response = loginController.login(loginRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            LoginResponse loginResponse = (LoginResponse) response.getBody();
            assertEquals("jwt-token-123", loginResponse.accessToken());
            assertEquals("ADMIN", loginResponse.role());
            verify(loginService, times(1)).login(loginRequest);
        }

        @Test
        @DisplayName("Should return null when user not found")
        void shouldReturnNullWhenUserNotFound() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "password123");
            when(loginService.login(loginRequest)).thenReturn(null);

            // Act
            ResponseEntity<?> response = loginController.login(loginRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(loginService, times(1)).login(loginRequest);
        }

        @Test
        @DisplayName("Should handle invalid login request")
        void shouldHandleInvalidLoginRequest() {
            // Arrange
            LoginRequest loginRequest = new LoginRequest("admin@example.com", "wrongpassword");
            LoginResponse invalidResponse = new LoginResponse(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    true,
                    null,
                    null,
                    null,
                    null
            );
            when(loginService.login(loginRequest)).thenReturn(invalidResponse);

            // Act
            ResponseEntity<?> response = loginController.login(loginRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            LoginResponse loginResponse = (LoginResponse) response.getBody();
            assertNull(loginResponse.accessToken());
            verify(loginService, times(1)).login(loginRequest);
        }
    }

    @Nested
    @DisplayName("logout Tests")
    class LogoutTests {

        @Test
        @DisplayName("Should logout successfully and invalidate session")
        void shouldLogoutSuccessfullyAndInvalidateSession() {
            // Arrange
            when(httpServletRequest.getSession()).thenReturn(httpSession);
            doNothing().when(httpSession).invalidate();

            // Act
            ResponseEntity<?> response = loginController.logout(httpServletRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(httpServletRequest, times(1)).getSession();
            verify(httpSession, times(1)).invalidate();
        }

        @Test
        @DisplayName("Should return empty response on successful logout")
        void shouldReturnEmptyResponseOnSuccessfulLogout() {
            // Arrange
            when(httpServletRequest.getSession()).thenReturn(httpSession);
            doNothing().when(httpSession).invalidate();

            // Act
            ResponseEntity<?> response = loginController.logout(httpServletRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getStatusCode().is2xxSuccessful());
        }

        @Test
        @DisplayName("Should handle null session")
        void shouldHandleNullSession() {
            // Arrange
            when(httpServletRequest.getSession()).thenReturn(null);

            // Act
            try {
                loginController.logout(httpServletRequest);
            } catch (NullPointerException e) {
                // Expected behavior when session is null
            }

            // Assert
            verify(httpServletRequest, times(1)).getSession();
        }
    }

    @Nested
    @DisplayName("profile Tests")
    class ProfileTests {

        @Test
        @DisplayName("Should return user profile successfully")
        void shouldReturnUserProfileSuccessfully() {
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

            LoginResponse profileResponse = new LoginResponse(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    true,
                    "avatar-url",
                    null,
                    "ADMIN",
                    List.of("USER_READ", "USER_WRITE")
            );

            when(loginService.profile(userLoginDetails)).thenReturn(profileResponse);

            // Act
            ResponseEntity<?> response = loginController.profile(userLoginDetails);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            LoginResponse result = (LoginResponse) response.getBody();
            assertEquals("admin@example.com", result.email());
            assertEquals("ADMIN", result.role());
            verify(loginService, times(1)).profile(userLoginDetails);
        }

        @Test
        @DisplayName("Should return profile with permissions")
        void shouldReturnProfileWithPermissions() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    "avatar-url",
                    true,
                    List.of("ROLE_ADMIN", "ADMIN", "USER_READ", "USER_WRITE", "CLIENT_DELETE")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            LoginResponse profileResponse = new LoginResponse(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    true,
                    "avatar-url",
                    null,
                    "ADMIN",
                    List.of("USER_READ", "USER_WRITE", "CLIENT_DELETE")
            );

            when(loginService.profile(userLoginDetails)).thenReturn(profileResponse);

            // Act
            ResponseEntity<?> response = loginController.profile(userLoginDetails);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            LoginResponse result = (LoginResponse) response.getBody();
            assertEquals(3, result.permissions().size());
            assertTrue(result.permissions().contains("USER_READ"));
            assertTrue(result.permissions().contains("CLIENT_DELETE"));
            verify(loginService, times(1)).profile(userLoginDetails);
        }

        @Test
        @DisplayName("Should handle null user login details")
        void shouldHandleNullUserLoginDetails() {
            // Arrange
            when(loginService.profile(null)).thenReturn(null);

            // Act
            ResponseEntity<?> response = loginController.profile(null);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(loginService, times(1)).profile(null);
        }

        @Test
        @DisplayName("Should return inactive user profile")
        void shouldReturnInactiveUserProfile() {
            // Arrange
            UserLogged userLogged = new UserLogged(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    "avatar-url",
                    false,
                    List.of("ROLE_ADMIN", "ADMIN")
            );
            UserLoginDetails userLoginDetails = new UserLoginDetails(userLogged);

            LoginResponse profileResponse = new LoginResponse(
                    testUserId,
                    "admin@example.com",
                    "Admin User",
                    false,
                    "avatar-url",
                    null,
                    "ADMIN",
                    List.of()
            );

            when(loginService.profile(userLoginDetails)).thenReturn(profileResponse);

            // Act
            ResponseEntity<?> response = loginController.profile(userLoginDetails);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            LoginResponse result = (LoginResponse) response.getBody();
            assertFalse(result.isActive());
            verify(loginService, times(1)).profile(userLoginDetails);
        }
    }
}


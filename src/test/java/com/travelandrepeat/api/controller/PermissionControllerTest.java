package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.service.PermissionService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PermissionController Tests")
class PermissionControllerTest {

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    private UUID testPermissionId;
    private Permission testPermission;

    @BeforeEach
    void setUp() {
        testPermissionId = UUID.randomUUID();

        testPermission = new Permission();
        testPermission.setPermissionId(testPermissionId);
        testPermission.setName("USER_READ");
        testPermission.setDescription("Lectura de usuarios");
        testPermission.setCreatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("getPermissionList Tests")
    class GetPermissionListTests {

        @Test
        @DisplayName("Should return list of all permissions")
        void shouldReturnListOfAllPermissions() {
            // Arrange
            List<Permission> permissionList = List.of(testPermission);
            when(permissionService.getPermissionList()).thenReturn(permissionList);

            // Act
            List<Permission> result = permissionController.getPermissionList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("USER_READ", result.get(0).getName());
            verify(permissionService, times(1)).getPermissionList();
        }

        @Test
        @DisplayName("Should return empty list when no permissions exist")
        void shouldReturnEmptyListWhenNoPermissionsExist() {
            // Arrange
            when(permissionService.getPermissionList()).thenReturn(new ArrayList<>());

            // Act
            List<Permission> result = permissionController.getPermissionList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(permissionService, times(1)).getPermissionList();
        }
    }

    @Nested
    @DisplayName("addPermission Tests")
    class AddPermissionTests {

        @Test
        @DisplayName("Should add new permission successfully")
        void shouldAddNewPermissionSuccessfully() {
            // Arrange
            Permission newPermission = new Permission();
            newPermission.setName("CLIENT_DELETE");
            newPermission.setDescription("Eliminar clientes");

            when(permissionService.addPermission(any(Permission.class))).thenReturn(testPermission);

            // Act
            Permission result = permissionController.addPermission(newPermission);

            // Assert
            assertNotNull(result);
            assertEquals("USER_READ", result.getName());
            verify(permissionService, times(1)).addPermission(any(Permission.class));
        }

        @Test
        @DisplayName("Should handle permission with Spanish characters")
        void shouldHandlePermissionWithSpanishCharacters() {
            // Arrange
            Permission newPermission = new Permission();
            newPermission.setName("CUSTOM_PERMISSION");
            newPermission.setDescription("Descripción con caracteres: ñ, á, é");

            Permission savedPermission = new Permission();
            savedPermission.setPermissionId(UUID.randomUUID());
            savedPermission.setName("CUSTOM_PERMISSION");
            savedPermission.setDescription("Descripción con caracteres: ñ, á, é");
            savedPermission.setCreatedAt(LocalDateTime.now());

            when(permissionService.addPermission(any(Permission.class))).thenReturn(savedPermission);

            // Act
            Permission result = permissionController.addPermission(newPermission);

            // Assert
            assertNotNull(result);
            assertTrue(result.getDescription().contains("ñ"));
            verify(permissionService, times(1)).addPermission(any(Permission.class));
        }
    }

    @Nested
    @DisplayName("updatePermission Tests")
    class UpdatePermissionTests {

        @Test
        @DisplayName("Should update existing permission successfully")
        void shouldUpdateExistingPermissionSuccessfully() {
            // Arrange
            Permission updatedPermission = new Permission();
            updatedPermission.setPermissionId(testPermissionId);
            updatedPermission.setName("USER_ADMIN");
            updatedPermission.setDescription("Administración de usuarios");

            when(permissionService.updatePermission(any(Permission.class))).thenReturn(updatedPermission);

            // Act
            Permission result = permissionController.updatePermission(updatedPermission);

            // Assert
            assertNotNull(result);
            assertEquals("USER_ADMIN", result.getName());
            verify(permissionService, times(1)).updatePermission(any(Permission.class));
        }
    }

    @Nested
    @DisplayName("deletePermission Tests")
    class DeletePermissionTests {

        @Test
        @DisplayName("Should delete permission successfully")
        void shouldDeletePermissionSuccessfully() {
            // Arrange
            when(permissionService.removePermission(testPermissionId)).thenReturn(testPermissionId);

            // Act
            ResponseEntity<UUID> response = permissionController.deleteProvider(testPermissionId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(testPermissionId, response.getBody());
            verify(permissionService, times(1)).removePermission(testPermissionId);
        }

        @Test
        @DisplayName("Should return deleted permission id")
        void shouldReturnDeletedPermissionId() {
            // Arrange
            UUID permissionIdToDelete = UUID.randomUUID();
            when(permissionService.removePermission(permissionIdToDelete)).thenReturn(permissionIdToDelete);

            // Act
            ResponseEntity<UUID> response = permissionController.deleteProvider(permissionIdToDelete);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(permissionIdToDelete, response.getBody());
            verify(permissionService, times(1)).removePermission(permissionIdToDelete);
        }

        @Test
        @DisplayName("Should handle non-existent permission deletion")
        void shouldHandleNonExistentPermissionDeletion() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(permissionService.removePermission(nonExistentId)).thenReturn(nonExistentId);

            // Act
            ResponseEntity<UUID> response = permissionController.deleteProvider(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(nonExistentId, response.getBody());
            verify(permissionService, times(1)).removePermission(nonExistentId);
        }
    }
}


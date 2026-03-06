package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.entity.RolePermissionId;
import com.travelandrepeat.api.repository.PermissionRepo;
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
@DisplayName("PermissionService Tests")
class PermissionServiceTest {

    @Mock
    private PermissionRepo permissionRepo;

    @InjectMocks
    private PermissionServiceImpl permissionService;

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
            Permission permission2 = new Permission();
            permission2.setPermissionId(UUID.randomUUID());
            permission2.setName("USER_WRITE");
            permission2.setDescription("Escritura de usuarios");
            permission2.setCreatedAt(LocalDateTime.now());

            List<Permission> permissionList = List.of(testPermission, permission2);
            when(permissionRepo.findAll()).thenReturn(permissionList);

            // Act
            List<Permission> result = permissionService.getPermissionList();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("USER_READ", result.get(0).getName());
            assertEquals("USER_WRITE", result.get(1).getName());
            verify(permissionRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no permissions exist")
        void shouldReturnEmptyListWhenNoPermissionsExist() {
            // Arrange
            when(permissionRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<Permission> result = permissionService.getPermissionList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(permissionRepo, times(1)).findAll();
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

            Permission savedPermission = new Permission();
            savedPermission.setPermissionId(UUID.randomUUID());
            savedPermission.setName("CLIENT_DELETE");
            savedPermission.setDescription("Eliminar clientes");
            savedPermission.setCreatedAt(LocalDateTime.now());

            when(permissionRepo.save(any(Permission.class))).thenReturn(savedPermission);

            // Act
            Permission result = permissionService.addPermission(newPermission);

            // Assert
            assertNotNull(result);
            assertEquals("CLIENT_DELETE", result.getName());
            assertEquals("Eliminar clientes", result.getDescription());
            verify(permissionRepo, times(1)).save(any(Permission.class));
        }

        @Test
        @DisplayName("Should handle permission with special characters in name")
        void shouldHandlePermissionWithSpecialCharactersInName() {
            // Arrange
            Permission newPermission = new Permission();
            newPermission.setName("CLIENT_DELETE_CUSTOM");
            newPermission.setDescription("Descripción con caracteres especiales: ñ, á, é");

            Permission savedPermission = new Permission();
            savedPermission.setPermissionId(UUID.randomUUID());
            savedPermission.setName("CLIENT_DELETE_CUSTOM");
            savedPermission.setDescription("Descripción con caracteres especiales: ñ, á, é");
            savedPermission.setCreatedAt(LocalDateTime.now());

            when(permissionRepo.save(any(Permission.class))).thenReturn(savedPermission);

            // Act
            Permission result = permissionService.addPermission(newPermission);

            // Assert
            assertNotNull(result);
            assertTrue(result.getDescription().contains("ñ"));
            verify(permissionRepo, times(1)).save(any(Permission.class));
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
            updatedPermission.setDescription("Administración completa de usuarios");

            when(permissionRepo.findById(testPermissionId)).thenReturn(Optional.of(updatedPermission));
            when(permissionRepo.save(any(Permission.class))).thenReturn(updatedPermission);

            // Act
            Permission result = permissionService.updatePermission(updatedPermission);

            // Assert
            assertNotNull(result);
            assertEquals("USER_ADMIN", result.getName());
            assertEquals("Administración completa de usuarios", result.getDescription());
            verify(permissionRepo, times(1)).save(any(Permission.class));
        }

        @Test
        @DisplayName("Should update permission description only")
        void shouldUpdatePermissionDescriptionOnly() {
            // Arrange
            testPermission.setDescription("Descripción actualizada");

            when(permissionRepo.findById(testPermissionId)).thenReturn(Optional.of(testPermission));
            when(permissionRepo.save(any(Permission.class))).thenReturn(testPermission);

            // Act
            Permission result = permissionService.updatePermission(testPermission);

            // Assert
            assertNotNull(result);
            assertEquals("Descripción actualizada", result.getDescription());
            verify(permissionRepo, times(1)).save(any(Permission.class));
        }
    }

    @Nested
    @DisplayName("removePermission Tests")
    class RemovePermissionTests {

        @Test
        @DisplayName("Should delete permission successfully")
        void shouldDeletePermissionSuccessfully() {
            // Arrange
            when(permissionRepo.findById(testPermissionId)).thenReturn(Optional.of(testPermission));
            doNothing().when(permissionRepo).deleteById(testPermissionId);

            // Act
            UUID result = permissionService.removePermission(testPermissionId);

            // Assert
            assertEquals(testPermissionId, result);
        }

        @Test
        @DisplayName("Should return the deleted permission id")
        void shouldReturnTheDeletedPermissionId() {
            // Arrange
            UUID permissionIdToDelete = UUID.randomUUID();
            testPermission.setPermissionId(permissionIdToDelete);
            when(permissionRepo.findById(permissionIdToDelete)).thenReturn(Optional.of(testPermission));
            doNothing().when(permissionRepo).deleteById(permissionIdToDelete);

            // Act
            UUID result = permissionService.removePermission(permissionIdToDelete);

            // Assert
            assertEquals(permissionIdToDelete, result);
            verify(permissionRepo, times(1)).deleteById(permissionIdToDelete);
        }

        @Test
        @DisplayName("Should handle deletion of non-existent permission")
        void shouldHandleDeletionOfNonExistentPermission() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            testPermission.setPermissionId(nonExistentId);
            when(permissionRepo.findById(nonExistentId)).thenReturn(Optional.of(testPermission));
            doNothing().when(permissionRepo).deleteById(nonExistentId);

            // Act
            UUID result = permissionService.removePermission(nonExistentId);

            // Assert
            assertEquals(nonExistentId, result);
            verify(permissionRepo, times(1)).deleteById(nonExistentId);
        }
    }
}


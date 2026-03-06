package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.entity.RolePermissionId;
import com.travelandrepeat.api.service.RoleService;
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
@DisplayName("RoleController Tests")
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private UUID testRoleId;
    private UUID testPermissionId;
    private Role testRole;
    private Permission testPermission;

    @BeforeEach
    void setUp() {
        testRoleId = UUID.randomUUID();
        testPermissionId = UUID.randomUUID();

        testRole = new Role();
        testRole.setRoleId(testRoleId);
        testRole.setName("ADMIN");
        testRole.setDescription("Administrador del sistema");
        testRole.setCreatedAt(LocalDateTime.now());

        testPermission = new Permission();
        testPermission.setPermissionId(testPermissionId);
        testPermission.setName("USER_READ");
        testPermission.setDescription("Lectura de usuarios");
    }

    @Nested
    @DisplayName("getRoleList Tests")
    class GetRoleListTests {

        @Test
        @DisplayName("Should return list of all roles")
        void shouldReturnListOfAllRoles() {
            // Arrange
            List<Role> roleList = List.of(testRole);
            when(roleService.getAll()).thenReturn(roleList);

            // Act
            List<Role> result = roleController.getRoleList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("ADMIN", result.get(0).getName());
            verify(roleService, times(1)).getAll();
        }

        @Test
        @DisplayName("Should return empty list when no roles exist")
        void shouldReturnEmptyListWhenNoRolesExist() {
            // Arrange
            when(roleService.getAll()).thenReturn(new ArrayList<>());

            // Act
            List<Role> result = roleController.getRoleList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(roleService, times(1)).getAll();
        }
    }

    @Nested
    @DisplayName("getRolePermissionList Tests")
    class GetRolePermissionListTests {

        @Test
        @DisplayName("Should return all role permissions")
        void shouldReturnAllRolePermissions() {
            // Arrange
            RolePermissionId rolePermissionId = new RolePermissionId();
            rolePermissionId.setRoleId(testRoleId);
            rolePermissionId.setPermissionId(testPermissionId);

            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(rolePermissionId.getPermissionId());
            rolePermission.setRoleId(rolePermissionId.getRoleId());

            List<RolePermission> rolePermissionList = List.of(rolePermission);
            when(roleService.getAllRolePermissions()).thenReturn(rolePermissionList);

            // Act
            List<RolePermission> result = roleController.getRolePermissionList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(roleService, times(1)).getAllRolePermissions();
        }

        @Test
        @DisplayName("Should return empty list when no role permissions exist")
        void shouldReturnEmptyListWhenNoRolePermissionsExist() {
            // Arrange
            when(roleService.getAllRolePermissions()).thenReturn(new ArrayList<>());

            // Act
            List<RolePermission> result = roleController.getRolePermissionList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(roleService, times(1)).getAllRolePermissions();
        }
    }

    @Nested
    @DisplayName("createRole Tests")
    class CreateRoleTests {

        @Test
        @DisplayName("Should create new role successfully")
        void shouldCreateNewRoleSuccessfully() {
            // Arrange
            Role newRole = new Role();
            newRole.setName("AUDITOR");
            newRole.setDescription("Auditor del sistema");

            when(roleService.createRole(any(Role.class))).thenReturn(testRole);

            // Act
            ResponseEntity<Role> response = roleController.createRole(newRole);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("ADMIN", response.getBody().getName());
            verify(roleService, times(1)).createRole(any(Role.class));
        }

        @Test
        @DisplayName("Should handle role with Spanish characters")
        void shouldHandleRoleWithSpanishCharacters() {
            // Arrange
            Role newRole = new Role();
            newRole.setName("CUSTOM_ROLE");
            newRole.setDescription("Descripción con caracteres: ñ, á, é");

            Role savedRole = new Role();
            savedRole.setRoleId(UUID.randomUUID());
            savedRole.setName("CUSTOM_ROLE");
            savedRole.setDescription("Descripción con caracteres: ñ, á, é");
            savedRole.setCreatedAt(LocalDateTime.now());

            when(roleService.createRole(any(Role.class))).thenReturn(savedRole);

            // Act
            ResponseEntity<Role> response = roleController.createRole(newRole);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getDescription().contains("ñ"));
            verify(roleService, times(1)).createRole(any(Role.class));
        }
    }

    @Nested
    @DisplayName("updateRole Tests")
    class UpdateRoleTests {

        @Test
        @DisplayName("Should update existing role successfully")
        void shouldUpdateExistingRoleSuccessfully() {
            // Arrange
            Role updatedRole = new Role();
            updatedRole.setRoleId(testRoleId);
            updatedRole.setName("ADMIN_UPDATED");
            updatedRole.setDescription("Administrador actualizado");

            when(roleService.updateRole(any(Role.class))).thenReturn(updatedRole);

            // Act
            ResponseEntity<Role> response = roleController.updateRole(updatedRole);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("ADMIN_UPDATED", response.getBody().getName());
            verify(roleService, times(1)).updateRole(any(Role.class));
        }
    }

    @Nested
    @DisplayName("deleteRole Tests")
    class DeleteRoleTests {

        @Test
        @DisplayName("Should delete role successfully")
        void shouldDeleteRoleSuccessfully() {
            // Arrange
            when(roleService.deleteRole(testRoleId)).thenReturn(testRoleId);

            // Act
            ResponseEntity<UUID> response = roleController.deleteRole(testRoleId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(testRoleId, response.getBody());
            verify(roleService, times(1)).deleteRole(testRoleId);
        }

        @Test
        @DisplayName("Should return deleted role id")
        void shouldReturnDeletedRoleId() {
            // Arrange
            UUID roleIdToDelete = UUID.randomUUID();
            when(roleService.deleteRole(roleIdToDelete)).thenReturn(roleIdToDelete);

            // Act
            ResponseEntity<UUID> response = roleController.deleteRole(roleIdToDelete);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(roleIdToDelete, response.getBody());
            verify(roleService, times(1)).deleteRole(roleIdToDelete);
        }

        @Test
        @DisplayName("Should handle non-existent role deletion")
        void shouldHandleNonExistentRoleDeletion() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(roleService.deleteRole(nonExistentId)).thenReturn(nonExistentId);

            // Act
            ResponseEntity<UUID> response = roleController.deleteRole(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(nonExistentId, response.getBody());
            verify(roleService, times(1)).deleteRole(nonExistentId);
        }
    }

    @Nested
    @DisplayName("addRolePermissionList Tests")
    class AddRolePermissionListTests {

        @Test
        @DisplayName("Should add multiple role permissions successfully")
        void shouldAddMultipleRolePermissionsSuccessfully() {
            // Arrange
            RolePermissionId rolePermissionId1 = new RolePermissionId();
            rolePermissionId1.setRoleId(testRoleId);
            rolePermissionId1.setPermissionId(testPermissionId);

            RolePermission rolePermission1 = new RolePermission();
            rolePermission1.setRoleId(rolePermissionId1.getRoleId());
            rolePermission1.setPermissionId(rolePermissionId1.getPermissionId());

            List<RolePermission> rolePermissionList = List.of(rolePermission1);
            when(roleService.addRolePermissionList(any())).thenReturn(rolePermissionList);

            // Act
            ResponseEntity<List<RolePermission>> response = roleController.createPermission(rolePermissionList);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            verify(roleService, times(1)).addRolePermissionList(any());
        }

        @Test
        @DisplayName("Should return empty list when adding empty role permission list")
        void shouldReturnEmptyListWhenAddingEmptyRolePermissionList() {
            // Arrange
            List<RolePermission> emptyList = new ArrayList<>();
            when(roleService.addRolePermissionList(any())).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<RolePermission>> response = roleController.createPermission(emptyList);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(roleService, times(1)).addRolePermissionList(any());
        }

        @Test
        @DisplayName("Should handle multiple role permissions with different roles")
        void shouldHandleMultipleRolePermissionsWithDifferentRoles() {
            // Arrange
            UUID roleId2 = UUID.randomUUID();
            UUID permissionId2 = UUID.randomUUID();

            RolePermissionId rolePermissionId1 = new RolePermissionId();
            rolePermissionId1.setRoleId(testRoleId);
            rolePermissionId1.setPermissionId(testPermissionId);

            RolePermissionId rolePermissionId2 = new RolePermissionId();
            rolePermissionId2.setRoleId(roleId2);
            rolePermissionId2.setPermissionId(permissionId2);

            RolePermission rolePermission1 = new RolePermission();
            rolePermission1.setPermissionId(rolePermissionId1.getPermissionId());
            rolePermission1.setRoleId(rolePermissionId1.getRoleId());

            RolePermission rolePermission2 = new RolePermission();
            rolePermission2.setPermissionId(rolePermissionId2.getPermissionId());
            rolePermission2.setRoleId(rolePermissionId2.getRoleId());

            List<RolePermission> rolePermissionList = List.of(rolePermission1, rolePermission2);
            when(roleService.addRolePermissionList(any())).thenReturn(rolePermissionList);

            // Act
            ResponseEntity<List<RolePermission>> response = roleController.createPermission(rolePermissionList);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(roleService, times(1)).addRolePermissionList(any());
        }
    }
}




package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.RolePermission;
import com.travelandrepeat.api.entity.RolePermissionId;
import com.travelandrepeat.api.repository.RolePermissionRepo;
import com.travelandrepeat.api.repository.RoleRepo;
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
@DisplayName("RoleService Tests")
class RoleServiceTest {

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private RolePermissionRepo rolePermissionRepo;

    @InjectMocks
    private RoleServiceImpl roleService;

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
    @DisplayName("getAll Tests")
    class GetAllTests {

        @Test
        @DisplayName("Should return list of all roles")
        void shouldReturnListOfAllRoles() {
            // Arrange
            Role role2 = new Role();
            role2.setRoleId(UUID.randomUUID());
            role2.setName("AGENT");
            role2.setDescription("Agente de viajes");
            role2.setCreatedAt(LocalDateTime.now());

            List<Role> roleList = List.of(testRole, role2);
            when(roleRepo.findAll()).thenReturn(roleList);

            // Act
            List<Role> result = roleService.getAll();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("ADMIN", result.get(0).getName());
            assertEquals("AGENT", result.get(1).getName());
            verify(roleRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no roles exist")
        void shouldReturnEmptyListWhenNoRolesExist() {
            // Arrange
            when(roleRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<Role> result = roleService.getAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(roleRepo, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getAllRolePermissions Tests")
    class GetAllRolePermissionsTests {

        @Test
        @DisplayName("Should return all role permissions")
        void shouldReturnAllRolePermissions() {
            // Arrange
            RolePermissionId rolePermissionId = new RolePermissionId();
            rolePermissionId.setRoleId(testRoleId);
            rolePermissionId.setPermissionId(testPermissionId);

            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(testRole.getRoleId());
            rolePermission.setPermissionId(testPermission.getPermissionId());

            List<RolePermission> rolePermissionList = List.of(rolePermission);
            when(rolePermissionRepo.findAll()).thenReturn(rolePermissionList);

            // Act
            List<RolePermission> result = roleService.getAllRolePermissions();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(testRoleId, result.get(0).getRoleId());
            verify(rolePermissionRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no role permissions exist")
        void shouldReturnEmptyListWhenNoRolePermissionsExist() {
            // Arrange
            when(rolePermissionRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<RolePermission> result = roleService.getAllRolePermissions();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(rolePermissionRepo, times(1)).findAll();
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

            Role savedRole = new Role();
            savedRole.setRoleId(UUID.randomUUID());
            savedRole.setName("AUDITOR");
            savedRole.setDescription("Auditor del sistema");
            savedRole.setCreatedAt(LocalDateTime.now());

            when(roleRepo.save(any(Role.class))).thenReturn(savedRole);

            // Act
            Role result = roleService.createRole(newRole);

            // Assert
            assertNotNull(result);
            assertEquals("AUDITOR", result.getName());
            assertEquals("Auditor del sistema", result.getDescription());
            verify(roleRepo, times(1)).save(any(Role.class));
        }

        @Test
        @DisplayName("Should handle role with Spanish characters in description")
        void shouldHandleRoleWithSpanishCharacters() {
            // Arrange
            Role newRole = new Role();
            newRole.setName("CUSTOM_ROLE");
            newRole.setDescription("Rol personalizado con caracteres: ñ, á, é, í, ó, ú");

            Role savedRole = new Role();
            savedRole.setRoleId(UUID.randomUUID());
            savedRole.setName("CUSTOM_ROLE");
            savedRole.setDescription("Rol personalizado con caracteres: ñ, á, é, í, ó, ú");
            savedRole.setCreatedAt(LocalDateTime.now());

            when(roleRepo.save(any(Role.class))).thenReturn(savedRole);

            // Act
            Role result = roleService.createRole(newRole);

            // Assert
            assertNotNull(result);
            assertTrue(result.getDescription().contains("ñ"));
            verify(roleRepo, times(1)).save(any(Role.class));
        }

        @Test
        @DisplayName("Should set createdAt timestamp when creating role")
        void shouldSetCreatedAtTimestampWhenCreatingRole() {
            // Arrange
            Role newRole = new Role();
            newRole.setName("NEW_ROLE");

            Role savedRole = new Role();
            savedRole.setRoleId(UUID.randomUUID());
            savedRole.setName("NEW_ROLE");
            savedRole.setCreatedAt(LocalDateTime.now());

            when(roleRepo.save(any(Role.class))).thenReturn(savedRole);

            // Act
            Role result = roleService.createRole(newRole);

            // Assert
            assertNotNull(result.getCreatedAt());
            verify(roleRepo, times(1)).save(any(Role.class));
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

            when(roleRepo.findById(testRoleId)).thenReturn(Optional.of(updatedRole));
            when(roleRepo.save(any(Role.class))).thenReturn(updatedRole);

            // Act
            Role result = roleService.updateRole(updatedRole);

            // Assert
            assertNotNull(result);
            assertEquals("ADMIN_UPDATED", result.getName());
            assertEquals("Administrador actualizado", result.getDescription());
            verify(roleRepo, times(1)).save(any(Role.class));
        }

        @Test
        @DisplayName("Should update role description only")
        void shouldUpdateRoleDescriptionOnly() {
            // Arrange
            testRole.setDescription("Nueva descripción");

            when(roleRepo.findById(testRoleId)).thenReturn(Optional.of(testRole));
            when(roleRepo.save(any(Role.class))).thenReturn(testRole);

            // Act
            Role result = roleService.updateRole(testRole);

            // Assert
            assertNotNull(result);
            assertEquals("Nueva descripción", result.getDescription());
            verify(roleRepo, times(1)).save(any(Role.class));
        }
    }

    @Nested
    @DisplayName("deleteRole Tests")
    class DeleteRoleTests {

        @Test
        @DisplayName("Should delete role successfully")
        void shouldDeleteRoleSuccessfully() {
            // Arrange

            when(roleRepo.findById(testRoleId)).thenReturn(Optional.of(testRole));
            doNothing().when(roleRepo).deleteById(testRoleId);

            // Act
            UUID result = roleService.deleteRole(testRoleId);

            // Assert
            assertEquals(testRoleId, result);
            verify(roleRepo, times(1)).deleteById(testRoleId);
        }

        @Test
        @DisplayName("Should return the deleted role id")
        void shouldReturnTheDeletedRoleId() {
            // Arrange
            UUID roleIdToDelete = UUID.randomUUID();
            testRole.setRoleId(roleIdToDelete);
            when(roleRepo.findById(roleIdToDelete)).thenReturn(Optional.of(testRole));
            doNothing().when(roleRepo).deleteById(roleIdToDelete);

            // Act
            UUID result = roleService.deleteRole(roleIdToDelete);

            // Assert
            assertEquals(roleIdToDelete, result);
            verify(roleRepo, times(1)).deleteById(roleIdToDelete);
        }
    }

    @Nested
    @DisplayName("addRolePermissionList Tests")
    class AddRolePermissionListTests {

        @Test
        @DisplayName("Should add multiple role permissions successfully")
        void shouldAddMultipleRolePermissionsSuccessfully() {
            // Arrange
            RolePermission rolePermission1 = new RolePermission();
            rolePermission1.setRoleId(testRole.getRoleId());
            rolePermission1.setPermissionId(testPermission.getPermissionId());

            UUID permissionId2 = UUID.randomUUID();
            RolePermissionId rolePermissionId2 = new RolePermissionId();
            rolePermissionId2.setRoleId(testRoleId);
            rolePermissionId2.setPermissionId(permissionId2);

            RolePermission rolePermission2 = new RolePermission();

            List<RolePermission> rolePermissionList = List.of(rolePermission1, rolePermission2);
            doNothing().when(rolePermissionRepo).deleteByRoleId(rolePermissionList.get(0).getRoleId());
            when(rolePermissionRepo.save(any())).thenReturn(any());

            // Act
            List<RolePermission> result = roleService.addRolePermissionList(rolePermissionList);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(rolePermissionRepo, times(2)).save(any());
        }

        @Test
        @DisplayName("Should add single role permission successfully")
        void shouldAddSingleRolePermissionSuccessfully() {
            // Arrange
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(testRole.getRoleId());
            rolePermission.setPermissionId(testPermission.getPermissionId());

            List<RolePermission> rolePermissionList = List.of(rolePermission);
            doNothing().when(rolePermissionRepo).deleteByRoleId(any());
            when(rolePermissionRepo.save(any())).thenReturn(any());

            // Act
            List<RolePermission> result = roleService.addRolePermissionList(rolePermissionList);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(testRoleId, result.get(0).getRoleId());
            verify(rolePermissionRepo, times(1)).save(any());
        }

        @Test
        @DisplayName("Should handle empty role permission list")
        void shouldHandleEmptyRolePermissionList() {
            // Arrange
            List<RolePermission> emptyList = new ArrayList<>();

            // Act
            List<RolePermission> result = roleService.addRolePermissionList(emptyList);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle duplicate role permission entries")
        void shouldHandleDuplicateRolePermissionEntries() {
            // Arrange
            RolePermissionId rolePermissionId = new RolePermissionId();
            rolePermissionId.setRoleId(testRoleId);
            rolePermissionId.setPermissionId(testPermissionId);

            RolePermission rolePermission1 = new RolePermission();
            rolePermission1.setPermissionId(rolePermissionId.getPermissionId());
            rolePermission1.setRoleId(rolePermissionId.getRoleId());

            RolePermission rolePermission2 = new RolePermission();
            rolePermission2.setPermissionId(rolePermissionId.getPermissionId());
            rolePermission2.setRoleId(rolePermissionId.getRoleId());

            List<RolePermission> rolePermissionList = List.of(rolePermission1, rolePermission2);
            doNothing().when(rolePermissionRepo).deleteByRoleId(any());
            when(rolePermissionRepo.save(any())).thenReturn(any());

            // Act
            List<RolePermission> result = roleService.addRolePermissionList(rolePermissionList);

            // Assert
            assertNotNull(result);
            verify(rolePermissionRepo, times(2)).save(any());
        }
    }
}


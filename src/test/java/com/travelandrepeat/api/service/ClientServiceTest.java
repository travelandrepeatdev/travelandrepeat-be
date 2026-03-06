package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.ClientRequest;
import com.travelandrepeat.api.dto.ClientResponse;
import com.travelandrepeat.api.entity.Client;
import com.travelandrepeat.api.repository.ClientRepo;
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
@DisplayName("ClientService Tests")
class ClientServiceTest {

    @Mock
    private ClientRepo clientRepo;

    @InjectMocks
    private ClientServiceImpl clientService;

    private UUID testClientId;
    private UUID testUserId;
    private Client testClient;
    private ClientRequest testClientRequest;

    @BeforeEach
    void setUp() {
        testClientId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        testClient = new Client();
        testClient.setId(testClientId);
        testClient.setName("Juan Pérez");
        testClient.setEmail("juan@example.com");
        testClient.setPhone("+34 666 123 456");
        testClient.setAddress("Calle Principal 123, Madrid");
        testClient.setCountryCode("ES");
        testClient.setNotes("Cliente frecuente");
        testClient.setCreatedBy(testUserId);
        testClient.setCreatedAt(LocalDateTime.now());
        testClient.setUpdatedAt(LocalDateTime.now());

        testClientRequest = new ClientRequest(
                testClientId,
                "Juan Pérez",
                "juan@example.com",
                "+34 666 123 456",
                "ES",
                "Calle Principal 123, Madrid",
                "Cliente frecuente",
                testUserId,
                LocalDateTime.now(),
                null
        );
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return list of all clients as ClientResponse")
        void shouldReturnListOfAllClientsAsClientResponse() {
            // Arrange
            Client client2 = new Client();
            client2.setId(UUID.randomUUID());
            client2.setName("Maria García");
            client2.setEmail("maria@example.com");
            client2.setPhone("+34 666 789 012");
            client2.setCountryCode("ES");
            client2.setCreatedBy(testUserId);
            client2.setCreatedAt(LocalDateTime.now());
            client2.setUpdatedAt(LocalDateTime.now());

            List<Client> clientList = List.of(testClient, client2);
            when(clientRepo.findAll()).thenReturn(clientList);

            // Act
            List<ClientResponse> result = clientService.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Juan Pérez", result.get(0).name());
            assertEquals("Maria García", result.get(1).name());
            verify(clientRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no clients exist")
        void shouldReturnEmptyListWhenNoClientsExist() {
            // Arrange
            when(clientRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<ClientResponse> result = clientService.findAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(clientRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should properly map client entity to response DTO")
        void shouldProperlyMapClientEntityToResponseDTO() {
            // Arrange
            when(clientRepo.findAll()).thenReturn(List.of(testClient));

            // Act
            List<ClientResponse> result = clientService.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            ClientResponse response = result.get(0);
            assertEquals("Juan Pérez", response.name());
            assertEquals("juan@example.com", response.email());
            assertEquals("+34 666 123 456", response.phone());
            assertEquals("Calle Principal 123, Madrid", response.address());
            assertEquals("ES", response.countryCode());
            assertEquals("Cliente frecuente", response.notes());
            assertNotNull(response.createdAt());
            assertNotNull(response.updatedAt());
        }
    }

    @Nested
    @DisplayName("addClient Tests")
    class AddClientTests {

        @Test
        @DisplayName("Should add new client successfully")
        void shouldAddNewClientSuccessfully() {
            // Arrange
            ClientRequest newClientRequest = new ClientRequest(
                    null,
                    "Nueva Cliente",
                    "nueva@example.com",
                    "+34 666 999 888",
                    "ES",
                    "Nueva Calle 456",
                    "Notas",
                    testUserId,
                    null,
                    null
            );

            Client savedClient = new Client();
            savedClient.setId(UUID.randomUUID());
            savedClient.setName("Nueva Cliente");
            savedClient.setEmail("nueva@example.com");
            savedClient.setCreatedBy(testUserId);
            savedClient.setCreatedAt(LocalDateTime.now());
            savedClient.setUpdatedAt(LocalDateTime.now());

            when(clientRepo.save(any(Client.class))).thenReturn(savedClient);

            // Act
            ClientResponse result = clientService.addClient(newClientRequest, false);

            // Assert
            assertNotNull(result);
            assertEquals("Nueva Cliente", result.name());
            assertEquals("nueva@example.com", result.email());
            verify(clientRepo, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Should set id to null when adding new client (isUpdate = false)")
        void shouldSetIdToNullWhenAddingNewClient() {
            // Arrange
            ClientRequest newClientRequest = new ClientRequest(
                    UUID.randomUUID(),
                    "Nueva Cliente",
                    "nueva@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    null,
                    null
            );

            Client savedClient = new Client();
            savedClient.setId(UUID.randomUUID());
            savedClient.setName("Nueva Cliente");
            savedClient.setCreatedBy(testUserId);
            savedClient.setCreatedAt(LocalDateTime.now());
            savedClient.setUpdatedAt(LocalDateTime.now());

            when(clientRepo.save(any(Client.class))).thenReturn(savedClient);

            // Act
            clientService.addClient(newClientRequest, false);

            // Assert
            verify(clientRepo, times(1)).save(argThat(client -> client.getId() == null));
        }

        @Test
        @DisplayName("Should set createdAt timestamp when adding new client")
        void shouldSetCreatedAtTimestampWhenAddingNewClient() {
            // Arrange
            ClientRequest newClientRequest = new ClientRequest(
                    null,
                    "Nueva Cliente",
                    "nueva@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    null,
                    null
            );

            Client savedClient = new Client();
            savedClient.setId(UUID.randomUUID());
            savedClient.setName("Nueva Cliente");
            savedClient.setCreatedBy(testUserId);
            savedClient.setCreatedAt(LocalDateTime.now());
            savedClient.setUpdatedAt(LocalDateTime.now());

            when(clientRepo.save(any(Client.class))).thenReturn(savedClient);

            // Act
            clientService.addClient(newClientRequest, false);

            // Assert
            verify(clientRepo, times(1)).save(argThat(client -> client.getCreatedAt() != null));
        }

        @Test
        @DisplayName("Should keep id when updating client (isUpdate = true)")
        void shouldKeepIdWhenUpdatingClient() {
            // Arrange
            ClientRequest updateClientRequest = new ClientRequest(
                    testClientId,
                    "Nombre Actualizado",
                    "updated@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    testClient.getCreatedAt(),
                    null
            );

            Client savedClient = new Client();
            savedClient.setId(testClientId);
            savedClient.setName("Nombre Actualizado");
            savedClient.setCreatedBy(testUserId);
            savedClient.setCreatedAt(testClient.getCreatedAt());
            savedClient.setUpdatedAt(LocalDateTime.now());

            when(clientRepo.save(any(Client.class))).thenReturn(savedClient);

            // Act
            clientService.addClient(updateClientRequest, true);

            // Assert
            verify(clientRepo, times(1)).save(argThat(client -> testClientId.equals(client.getId())));
        }
    }

    @Nested
    @DisplayName("removeClient Tests")
    class RemoveClientTests {

        @Test
        @DisplayName("Should delete client successfully")
        void shouldDeleteClientSuccessfully() {
            // Arrange
            doNothing().when(clientRepo).deleteById(testClientId);

            // Act
            boolean result = clientService.removeClient(testClientId);

            // Assert
            assertTrue(result);
            verify(clientRepo, times(1)).deleteById(testClientId);
        }

        @Test
        @DisplayName("Should handle deletion of non-existent client")
        void shouldHandleDeletionOfNonExistentClient() {
            // Arrange
            doNothing().when(clientRepo).deleteById(testClientId);

            // Act
            boolean result = clientService.removeClient(testClientId);

            // Assert
            assertTrue(result);
            verify(clientRepo, times(1)).deleteById(testClientId);
        }
    }

    @Nested
    @DisplayName("modifyClient Tests")
    class ModifyClientTests {

        @Test
        @DisplayName("Should update existing client successfully")
        void shouldUpdateExistingClientSuccessfully() {
            // Arrange
            ClientRequest updateRequest = new ClientRequest(
                    testClientId,
                    "Juan Pérez Actualizado",
                    "juan.actualizado@example.com",
                    "+34 666 123 999",
                    "ES",
                    "Nueva Dirección 789",
                    "Notas actualizadas",
                    testUserId,
                    testClient.getCreatedAt(),
                    null
            );

            Client updatedClient = new Client();
            updatedClient.setId(testClientId);
            updatedClient.setName("Juan Pérez Actualizado");
            updatedClient.setEmail("juan.actualizado@example.com");
            updatedClient.setPhone("+34 666 123 999");
            updatedClient.setAddress("Nueva Dirección 789");
            updatedClient.setCountryCode("ES");
            updatedClient.setNotes("Notas actualizadas");
            updatedClient.setCreatedBy(testUserId);
            updatedClient.setCreatedAt(testClient.getCreatedAt());
            updatedClient.setUpdatedAt(LocalDateTime.now());

            when(clientRepo.findById(testClientId)).thenReturn(Optional.of(testClient));
            when(clientRepo.save(any(Client.class))).thenReturn(updatedClient);

            // Act
            ClientResponse result = clientService.modifyClient(updateRequest, true);

            // Assert
            assertNotNull(result);
            assertEquals("Juan Pérez Actualizado", result.name());
            assertEquals("juan.actualizado@example.com", result.email());
            verify(clientRepo, times(1)).findById(testClientId);
            verify(clientRepo, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Should return null when client to update does not exist")
        void shouldReturnNullWhenClientToUpdateDoesNotExist() {
            // Arrange
            ClientRequest updateRequest = new ClientRequest(
                    testClientId,
                    "Juan Pérez",
                    "juan@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    null,
                    null
            );

            when(clientRepo.findById(testClientId)).thenReturn(Optional.empty());

            // Act
            ClientResponse result = clientService.modifyClient(updateRequest, true);

            // Assert
            assertNull(result);
            verify(clientRepo, times(1)).findById(testClientId);
            verify(clientRepo, never()).save(any(Client.class));
        }

        @Test
        @DisplayName("Should preserve createdAt and createdBy when updating")
        void shouldPreserveCreatedAtAndCreatedByWhenUpdating() {
            // Arrange
            ClientRequest updateRequest = new ClientRequest(
                    testClientId,
                    "Juan Pérez Actualizado",
                    "juan.actualizado@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    testClient.getCreatedAt(),
                    null
            );

            Client updatedClient = new Client();
            updatedClient.setId(testClientId);
            updatedClient.setCreatedBy(testUserId);
            updatedClient.setCreatedAt(testClient.getCreatedAt());
            updatedClient.setUpdatedAt(LocalDateTime.now());

            when(clientRepo.findById(testClientId)).thenReturn(Optional.of(testClient));
            when(clientRepo.save(any(Client.class))).thenReturn(updatedClient);

            // Act
            clientService.modifyClient(updateRequest, true);

            // Assert
            verify(clientRepo).save(argThat(client ->
                    testClient.getCreatedAt().equals(client.getCreatedAt()) &&
                    testUserId.equals(client.getCreatedBy())
            ));
        }

        @Test
        @DisplayName("Should update the updatedAt timestamp")
        void shouldUpdateTheUpdatedAtTimestamp() {
            // Arrange
            ClientRequest updateRequest = new ClientRequest(
                    testClientId,
                    "Juan Pérez",
                    "juan@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    testClient.getCreatedAt(),
                    null
            );

            Client updatedClient = new Client();
            updatedClient.setId(testClientId);
            updatedClient.setCreatedAt(testClient.getCreatedAt());
            updatedClient.setUpdatedAt(LocalDateTime.now());
            updatedClient.setCreatedBy(testUserId);

            when(clientRepo.findById(testClientId)).thenReturn(Optional.of(testClient));
            when(clientRepo.save(any(Client.class))).thenReturn(updatedClient);

            // Act
            clientService.modifyClient(updateRequest, true);

            // Assert
            verify(clientRepo).save(argThat(client -> client.getUpdatedAt() != null));
        }
    }
}


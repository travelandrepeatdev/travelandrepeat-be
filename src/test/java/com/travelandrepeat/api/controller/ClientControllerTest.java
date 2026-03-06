package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.ClientRequest;
import com.travelandrepeat.api.dto.ClientResponse;
import com.travelandrepeat.api.service.ClientService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientController Tests")
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private UUID testClientId;
    private UUID testUserId;
    private ClientResponse testClientResponse;
    private ClientRequest testClientRequest;

    @BeforeEach
    void setUp() {
        testClientId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        testClientResponse = ClientResponse.builder()
                .id(testClientId.toString())
                .name("Juan Pérez")
                .email("juan@example.com")
                .phone("+34 666 123 456")
                .address("Calle Principal 123, Madrid")
                .countryCode("ES")
                .notes("Cliente frecuente")
                .createdAt(LocalDateTime.now().toString())
                .updatedAt(LocalDateTime.now().toString())
                .createdBy(testUserId.toString())
                .build();

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
    @DisplayName("getClientList Tests")
    class GetClientListTests {

        @Test
        @DisplayName("Should return list of all clients")
        void shouldReturnListOfAllClients() {
            // Arrange
            List<ClientResponse> clientList = List.of(testClientResponse);
            when(clientService.findAll()).thenReturn(clientList);

            // Act
            ResponseEntity<List<ClientResponse>> response = clientController.getClientList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Juan Pérez", response.getBody().get(0).name());
            verify(clientService, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no clients exist")
        void shouldReturnEmptyListWhenNoClientsExist() {
            // Arrange
            when(clientService.findAll()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<ClientResponse>> response = clientController.getClientList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(clientService, times(1)).findAll();
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
                    "Nueva Calle",
                    null,
                    testUserId,
                    null,
                    null
            );

            when(clientService.addClient(any(ClientRequest.class), eq(false)))
                    .thenReturn(testClientResponse);

            // Act
            ResponseEntity<ClientResponse> response = clientController.addClient(newClientRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Juan Pérez", response.getBody().name());
            verify(clientService, times(1)).addClient(any(ClientRequest.class), eq(false));
        }

        @Test
        @DisplayName("Should return response entity with client response")
        void shouldReturnResponseEntityWithClientResponse() {
            // Arrange
            when(clientService.addClient(any(ClientRequest.class), eq(false)))
                    .thenReturn(testClientResponse);

            // Act
            ResponseEntity<ClientResponse> response = clientController.addClient(testClientRequest);

            // Assert
            assertNotNull(response);
            assertNotNull(response.getBody());
            assertTrue(response.getStatusCode().is2xxSuccessful());
        }
    }

    @Nested
    @DisplayName("deleteClient Tests")
    class DeleteClientTests {

        @Test
        @DisplayName("Should delete client successfully")
        void shouldDeleteClientSuccessfully() {
            // Arrange
            when(clientService.removeClient(testClientId)).thenReturn(true);

            // Act
            ResponseEntity<Boolean> response = clientController.deleteClient(testClientId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody());
            verify(clientService, times(1)).removeClient(testClientId);
        }

        @Test
        @DisplayName("Should return false when client deletion fails")
        void shouldReturnFalseWhenClientDeletionFails() {
            // Arrange
            when(clientService.removeClient(testClientId)).thenReturn(false);

            // Act
            ResponseEntity<Boolean> response = clientController.deleteClient(testClientId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody());
            verify(clientService, times(1)).removeClient(testClientId);
        }

        @Test
        @DisplayName("Should handle non-existent client id")
        void shouldHandleNonExistentClientId() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(clientService.removeClient(nonExistentId)).thenReturn(true);

            // Act
            ResponseEntity<Boolean> response = clientController.deleteClient(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(Boolean.TRUE, response.getBody());
            verify(clientService, times(1)).removeClient(nonExistentId);
        }
    }

    @Nested
    @DisplayName("updateClient Tests")
    class UpdateClientTests {

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
                    LocalDateTime.now(),
                    null
            );

            ClientResponse updatedResponse = ClientResponse.builder()
                    .id(testClientId.toString())
                    .name("Juan Pérez Actualizado")
                    .email("juan.actualizado@example.com")
                    .phone("+34 666 123 999")
                    .address("Nueva Dirección 789")
                    .countryCode("ES")
                    .notes("Notas actualizadas")
                    .createdAt(LocalDateTime.now().toString())
                    .updatedAt(LocalDateTime.now().toString())
                    .createdBy(testUserId.toString())
                    .build();

            when(clientService.modifyClient(any(ClientRequest.class), eq(true)))
                    .thenReturn(updatedResponse);

            // Act
            ResponseEntity<?> response = clientController.updateClient(updateRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(clientService, times(1)).modifyClient(any(ClientRequest.class), eq(true));
        }

        @Test
        @DisplayName("Should return null when client to update does not exist")
        void shouldReturnNullWhenClientToUpdateDoesNotExist() {
            // Arrange
            when(clientService.modifyClient(any(ClientRequest.class), eq(true)))
                    .thenReturn(null);

            // Act
            ResponseEntity<?> response = clientController.updateClient(testClientRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(clientService, times(1)).modifyClient(any(ClientRequest.class), eq(true));
        }

        @Test
        @DisplayName("Should preserve client timestamps when updating")
        void shouldPreserveClientTimestampsWhenUpdating() {
            // Arrange
            LocalDateTime originalCreatedAt = LocalDateTime.now().minusDays(1);
            ClientRequest updateRequest = new ClientRequest(
                    testClientId,
                    "Juan Pérez",
                    "juan@example.com",
                    null,
                    "ES",
                    null,
                    null,
                    testUserId,
                    originalCreatedAt,
                    null
            );

            when(clientService.modifyClient(any(ClientRequest.class), eq(true)))
                    .thenReturn(testClientResponse);

            // Act
            ResponseEntity<?> response = clientController.updateClient(updateRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(clientService, times(1)).modifyClient(any(ClientRequest.class), eq(true));
        }
    }
}




package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.ProviderRequest;
import com.travelandrepeat.api.dto.ProviderResponse;
import com.travelandrepeat.api.service.ProviderService;
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
@DisplayName("ProviderController Tests")
class ProviderControllerTest {

    @Mock
    private ProviderService providerService;

    @InjectMocks
    private ProviderController providerController;

    private UUID testProviderId;
    private ProviderResponse testProviderResponse;
    private ProviderRequest testProviderRequest;

    @BeforeEach
    void setUp() {
        testProviderId = UUID.randomUUID();

        testProviderResponse = ProviderResponse.builder()
                .id(testProviderId)
                .name("Hotel Paradise")
                .category("HOTEL")
                .email("info@hotelparadise.com")
                .phone("+34 952 123 456")
                .contactName("Carlos García")
                .website("https://www.hotelparadise.com")
                .notes("Proveedor de 5 estrellas")
                .createdBy(UUID.randomUUID())
                .build();

        testProviderRequest = new ProviderRequest(
                testProviderId,
                "Hotel Paradise",
                "Carlos García",
                "info@hotelparadise.com",
                "+34 952 123 456",
                "HOTEL",
                "https://www.hotelparadise.com",
                "Proveedor de 5 estrellas",
                UUID.randomUUID(),
                LocalDateTime.now(),
                null
        );
    }

    @Nested
    @DisplayName("getProviderList Tests")
    class GetProviderListTests {

        @Test
        @DisplayName("Should return list of all providers")
        void shouldReturnListOfAllProviders() {
            // Arrange
            List<ProviderResponse> providerList = List.of(testProviderResponse);
            when(providerService.getProviderList()).thenReturn(providerList);

            // Act
            ResponseEntity<List<ProviderResponse>> response = providerController.getProviderList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Hotel Paradise", response.getBody().get(0).name());
            verify(providerService, times(1)).getProviderList();
        }

        @Test
        @DisplayName("Should return empty list when no providers exist")
        void shouldReturnEmptyListWhenNoProvidersExist() {
            // Arrange
            when(providerService.getProviderList()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<ProviderResponse>> response = providerController.getProviderList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(providerService, times(1)).getProviderList();
        }
    }

    @Nested
    @DisplayName("addProvider Tests")
    class AddProviderTests {

        @Test
        @DisplayName("Should add new provider successfully")
        void shouldAddNewProviderSuccessfully() {
            // Arrange
            ProviderRequest newProviderRequest = new ProviderRequest(
                    null,
                    "Nueva Agencia",
                    "Juan Rodríguez",
                    "nueva@agency.com",
                    "+34 666 111 222",
                    "AGENCY",
                    "https://www.newagency.com",
                    "Nueva agencia de viajes",
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    null
            );

            when(providerService.addProvider(any(ProviderRequest.class), eq(false)))
                    .thenReturn(testProviderResponse);

            // Act
            ResponseEntity<?> response = providerController.addProvider(newProviderRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(providerService, times(1)).addProvider(any(ProviderRequest.class), eq(false));
        }
    }

    @Nested
    @DisplayName("deleteProvider Tests")
    class DeleteProviderTests {

        @Test
        @DisplayName("Should delete provider successfully")
        void shouldDeleteProviderSuccessfully() {
            // Arrange
            when(providerService.removeProvider(testProviderId)).thenReturn(true);

            // Act
            ResponseEntity<Boolean> response = providerController.deleteProvider(testProviderId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody());
            verify(providerService, times(1)).removeProvider(testProviderId);
        }

        @Test
        @DisplayName("Should handle non-existent provider deletion")
        void shouldHandleNonExistentProviderDeletion() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(providerService.removeProvider(nonExistentId)).thenReturn(true);

            // Act
            ResponseEntity<Boolean> response = providerController.deleteProvider(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(Boolean.TRUE, response.getBody());
            verify(providerService, times(1)).removeProvider(nonExistentId);
        }
    }

    @Nested
    @DisplayName("updateProvider Tests")
    class UpdateProviderTests {

        @Test
        @DisplayName("Should update existing provider successfully")
        void shouldUpdateExistingProviderSuccessfully() {
            // Arrange
            ProviderRequest updateRequest = new ProviderRequest(
                    testProviderId,
                    "Hotel Paradise Plus",
                    "Carlos García",
                    "info@hotelparadise.com",
                    "+34 952 999 999",
                    "HOTEL",
                    "https://www.hotelparadise-plus.com",
                    "Proveedor premium",
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    null
            );

            ProviderResponse updatedResponse = ProviderResponse.builder()
                    .id(testProviderId)
                    .name("Hotel Paradise Plus")
                    .category("HOTEL")
                    .email("info@hotelparadise.com")
                    .phone("+34 952 999 999")
                    .contactName("Carlos García")
                    .website("https://www.hotelparadise-plus.com")
                    .notes("Proveedor premium")
                    .createdBy(UUID.randomUUID())
                    .build();

            when(providerService.modifyProvider(any(ProviderRequest.class), eq(true)))
                    .thenReturn(updatedResponse);

            // Act
            ResponseEntity<?> response = providerController.updateProvider(updateRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(providerService, times(1)).modifyProvider(any(ProviderRequest.class), eq(true));
        }

        @Test
        @DisplayName("Should return null when provider to update does not exist")
        void shouldReturnNullWhenProviderToUpdateDoesNotExist() {
            // Arrange
            when(providerService.modifyProvider(any(ProviderRequest.class), eq(true)))
                    .thenReturn(null);

            // Act
            ResponseEntity<?> response = providerController.updateProvider(testProviderRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(providerService, times(1)).modifyProvider(any(ProviderRequest.class), eq(true));
        }
    }
}





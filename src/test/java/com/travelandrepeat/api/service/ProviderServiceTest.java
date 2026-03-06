package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.ProviderRequest;
import com.travelandrepeat.api.dto.ProviderResponse;
import com.travelandrepeat.api.entity.Provider;
import com.travelandrepeat.api.repository.ProviderRepo;
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
@DisplayName("ProviderService Tests")
class ProviderServiceTest {

    @Mock
    private ProviderRepo providerRepo;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private UUID testProviderId;
    private Provider testProvider;
    private ProviderRequest testProviderRequest;

    @BeforeEach
    void setUp() {
        testProviderId = UUID.randomUUID();

        testProvider = new Provider();
        testProvider.setId(testProviderId);
        testProvider.setName("Hotel Paradise");
        testProvider.setCategory("HOTEL");
        testProvider.setEmail("info@hotelparadise.com");
        testProvider.setPhone("+34 952 123 456");
        testProvider.setContactName("Carlos García");
        testProvider.setWebsite("https://www.hotelparadise.com");
        testProvider.setNotes("Proveedor de 5 estrellas");
        testProvider.setCreatedAt(LocalDateTime.now());
        testProvider.setUpdatedAt(LocalDateTime.now());

        testProviderRequest = new ProviderRequest(
                testProviderId,
                "Hotel Paradise",
                "HOTEL",
                "info@hotelparadise.com",
                "+34 952 123 456",
                "Carlos García",
                "https://www.hotelparadise.com",
                "Proveedor de 5 estrellas",
                UUID.randomUUID(),
                null,
                null
        );
    }

    @Nested
    @DisplayName("getProviderList Tests")
    class GetProviderListTests {

        @Test
        @DisplayName("Should return list of all providers as ProviderResponse")
        void shouldReturnListOfAllProvidersAsProviderResponse() {
            // Arrange
            Provider provider2 = new Provider();
            provider2.setId(UUID.randomUUID());
            provider2.setName("Tour Operators Plus");
            provider2.setCategory("TOUR");
            provider2.setEmail("info@tourplus.com");
            provider2.setPhone("+34 934 567 890");
            provider2.setContactName("María López");
            provider2.setWebsite("https://www.tourplus.com");
            provider2.setCreatedAt(LocalDateTime.now());
            provider2.setUpdatedAt(LocalDateTime.now());

            List<Provider> providerList = List.of(testProvider, provider2);
            when(providerRepo.findAll()).thenReturn(providerList);

            // Act
            List<ProviderResponse> result = providerService.getProviderList();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Hotel Paradise", result.get(0).name());
            assertEquals("Tour Operators Plus", result.get(1).name());
            verify(providerRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no providers exist")
        void shouldReturnEmptyListWhenNoProvidersExist() {
            // Arrange
            when(providerRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<ProviderResponse> result = providerService.getProviderList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(providerRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should properly map provider entity to response DTO")
        void shouldProperlyMapProviderEntityToResponseDTO() {
            // Arrange
            when(providerRepo.findAll()).thenReturn(List.of(testProvider));

            // Act
            List<ProviderResponse> result = providerService.getProviderList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            ProviderResponse response = result.get(0);
            assertEquals("Hotel Paradise", response.name());
            assertEquals("HOTEL", response.category());
            assertEquals("info@hotelparadise.com", response.email());
            assertEquals("+34 952 123 456", response.phone());
            assertEquals("Carlos García", response.contactName());
            assertEquals("https://www.hotelparadise.com", response.website());
            assertEquals("Proveedor de 5 estrellas", response.notes());
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
                    "AGENCY",
                    "nueva@agency.com",
                    "+34 666 111 222",
                    "Juan Rodríguez",
                    "https://www.newagency.com",
                    "Nueva agencia de viajes",
                    UUID.randomUUID(),
                    null,
                    null
            );

            Provider savedProvider = new Provider();
            savedProvider.setId(UUID.randomUUID());
            savedProvider.setName("Nueva Agencia");
            savedProvider.setCategory("AGENCY");
            savedProvider.setEmail("nueva@agency.com");
            savedProvider.setCreatedAt(LocalDateTime.now());
            savedProvider.setUpdatedAt(LocalDateTime.now());

            when(providerRepo.save(any(Provider.class))).thenReturn(savedProvider);

            // Act
            ProviderResponse result = providerService.addProvider(newProviderRequest, false);

            // Assert
            assertNotNull(result);
            assertEquals("Nueva Agencia", result.name());
            assertEquals("AGENCY", result.category());
            verify(providerRepo, times(1)).save(any(Provider.class));
        }

        @Test
        @DisplayName("Should set id to null when adding new provider (isUpdate = false)")
        void shouldSetIdToNullWhenAddingNewProvider() {
            // Arrange
            ProviderRequest newProviderRequest = new ProviderRequest(
                    UUID.randomUUID(),
                    "Nueva Agencia",
                    "AGENCY",
                    "nueva@agency.com",
                    null,
                    null,
                    null,
                    null,
                    UUID.randomUUID(),
                    null,
                    null
            );

            Provider savedProvider = new Provider();
            savedProvider.setId(UUID.randomUUID());
            savedProvider.setName("Nueva Agencia");
            savedProvider.setCreatedAt(LocalDateTime.now());
            savedProvider.setUpdatedAt(LocalDateTime.now());

            when(providerRepo.save(any(Provider.class))).thenReturn(savedProvider);

            // Act
            providerService.addProvider(newProviderRequest, false);

            // Assert
            verify(providerRepo, times(1)).save(argThat(provider -> provider.getId() == null));
        }

        @Test
        @DisplayName("Should keep id when updating provider (isUpdate = true)")
        void shouldKeepIdWhenUpdatingProvider() {
            // Arrange
            ProviderRequest updateProviderRequest = new ProviderRequest(
                    testProviderId,
                    "Hotel Paradise Actualizado",
                    "HOTEL",
                    "info@hotelparadise.com",
                    "+34 952 123 456",
                    "Carlos García",
                    "https://www.hotelparadise.com",
                    "Proveedor actualizado",
                    UUID.randomUUID(),
                    null,
                    null
            );

            Provider savedProvider = new Provider();
            savedProvider.setId(testProviderId);
            savedProvider.setName("Hotel Paradise Actualizado");
            savedProvider.setCreatedAt(LocalDateTime.now());
            savedProvider.setUpdatedAt(LocalDateTime.now());

            when(providerRepo.save(any(Provider.class))).thenReturn(savedProvider);

            // Act
            providerService.addProvider(updateProviderRequest, true);

            // Assert
            verify(providerRepo, times(1)).save(argThat(provider -> testProviderId.equals(provider.getId())));
        }

        @Test
        @DisplayName("Should handle null optional fields")
        void shouldHandleNullOptionalFields() {
            // Arrange
            ProviderRequest newProviderRequest = new ProviderRequest(
                    null,
                    "Proveedor Minimo",
                    "OTHER",
                    "minimo@provider.com",
                    null,
                    null,
                    null,
                    null,
                    UUID.randomUUID(),
                    null,
                    null
            );

            Provider savedProvider = new Provider();
            savedProvider.setId(UUID.randomUUID());
            savedProvider.setName("Proveedor Minimo");
            savedProvider.setCreatedAt(LocalDateTime.now());
            savedProvider.setUpdatedAt(LocalDateTime.now());

            when(providerRepo.save(any(Provider.class))).thenReturn(savedProvider);

            // Act
            ProviderResponse result = providerService.addProvider(newProviderRequest, false);

            // Assert
            assertNotNull(result);
            assertEquals("Proveedor Minimo", result.name());
            verify(providerRepo, times(1)).save(any(Provider.class));
        }
    }

    @Nested
    @DisplayName("removeProvider Tests")
    class RemoveProviderTests {

        @Test
        @DisplayName("Should delete provider successfully")
        void shouldDeleteProviderSuccessfully() {
            // Arrange
            doNothing().when(providerRepo).deleteById(testProviderId);

            // Act
            boolean result = providerService.removeProvider(testProviderId);

            // Assert
            assertTrue(result);
            verify(providerRepo, times(1)).deleteById(testProviderId);
        }

        @Test
        @DisplayName("Should handle deletion of non-existent provider")
        void shouldHandleDeletionOfNonExistentProvider() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            doNothing().when(providerRepo).deleteById(nonExistentId);

            // Act
            boolean result = providerService.removeProvider(nonExistentId);

            // Assert
            assertTrue(result);
            verify(providerRepo, times(1)).deleteById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("modifyProvider Tests")
    class ModifyProviderTests {

        @Test
        @DisplayName("Should update existing provider successfully")
        void shouldUpdateExistingProviderSuccessfully() {
            // Arrange
            ProviderRequest updateRequest = new ProviderRequest(
                    testProviderId,
                    "Hotel Paradise Plus",
                    "HOTEL",
                    "info@hotelparadise.com",
                    "+34 952 999 999",
                    "Carlos García",
                    "https://www.hotelparadise-plus.com",
                    "Proveedor premium",
                    UUID.randomUUID(),
                    null,
                    null
            );

            Provider updatedProvider = new Provider();
            updatedProvider.setId(testProviderId);
            updatedProvider.setName("Hotel Paradise Plus");
            updatedProvider.setCategory("HOTEL");
            updatedProvider.setPhone("+34 952 999 999");
            updatedProvider.setWebsite("https://www.hotelparadise-plus.com");
            updatedProvider.setNotes("Proveedor premium");
            updatedProvider.setCreatedAt(testProvider.getCreatedAt());
            updatedProvider.setUpdatedAt(LocalDateTime.now());

            when(providerRepo.findById(testProviderId)).thenReturn(Optional.of(testProvider));
            when(providerRepo.save(any(Provider.class))).thenReturn(updatedProvider);

            // Act
            ProviderResponse result = providerService.modifyProvider(updateRequest, true);

            // Assert
            assertNotNull(result);
            assertEquals("Hotel Paradise Plus", result.name());
            assertEquals("+34 952 999 999", result.phone());
            verify(providerRepo, times(1)).findById(testProviderId);
            verify(providerRepo, times(1)).save(any(Provider.class));
        }

        @Test
        @DisplayName("Should return null when provider to update does not exist")
        void shouldReturnNullWhenProviderToUpdateDoesNotExist() {
            // Arrange
            ProviderRequest updateRequest = new ProviderRequest(
                    testProviderId,
                    "Hotel Paradise",
                    "HOTEL",
                    "info@hotelparadise.com",
                    null,
                    null,
                    null,
                    null,
                    UUID.randomUUID(),
                    null,
                    null
            );

            when(providerRepo.findById(testProviderId)).thenReturn(Optional.empty());

            // Act
            ProviderResponse result = providerService.modifyProvider(updateRequest, true);

            // Assert
            assertNull(result);
            verify(providerRepo, times(1)).findById(testProviderId);
            verify(providerRepo, never()).save(any(Provider.class));
        }

        @Test
        @DisplayName("Should update only provided fields")
        void shouldUpdateOnlyProvidedFields() {
            // Arrange
            ProviderRequest updateRequest = new ProviderRequest(
                    testProviderId,
                    "Hotel Paradise Actualizado",
                    "HOTEL",
                    "info@hotelparadise.com",
                    "+34 952 888 777",
                    "Carlos García",
                    "https://www.hotelparadise.com",
                    "Actualizado",
                    UUID.randomUUID(),
                    null,
                    null
            );

            Provider updatedProvider = new Provider();
            updatedProvider.setId(testProviderId);
            updatedProvider.setName("Hotel Paradise Actualizado");
            updatedProvider.setPhone("+34 952 888 777");
            updatedProvider.setNotes("Actualizado");
            updatedProvider.setCreatedAt(testProvider.getCreatedAt());
            updatedProvider.setUpdatedAt(LocalDateTime.now());

            when(providerRepo.findById(testProviderId)).thenReturn(Optional.of(testProvider));
            when(providerRepo.save(any(Provider.class))).thenReturn(updatedProvider);

            // Act
            providerService.modifyProvider(updateRequest, true);

            // Assert
            verify(providerRepo).save(argThat(provider ->
                    "Hotel Paradise Actualizado".equals(provider.getName()) &&
                    "+34 952 888 777".equals(provider.getPhone())
            ));
        }
    }
}


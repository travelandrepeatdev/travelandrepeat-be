package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.service.PromotionService;
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
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PromotionController Tests")
class PromotionControllerTest {

    @Mock
    private PromotionService promotionService;

    @Mock
    private MultipartFile mockImage;

    @InjectMocks
    private PromotionController promotionController;

    private UUID testPromotionId;
    private PromotionResponse testPromotionResponse;
    private PromotionRequest testPromotionRequest;

    @BeforeEach
    void setUp() {
        testPromotionId = UUID.randomUUID();

        testPromotionResponse = PromotionResponse.builder()
                .id(testPromotionId)
                .title("Summer Vacation Special")
                .description("Amazing summer vacation deals")
                .destination("Spain")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .imageUrl("https://example.com/image.jpg")
                .isActive(true)
                .build();

        testPromotionRequest = new PromotionRequest(
                testPromotionId,
                "Summer Vacation Special",
                "Amazing summer vacation deals",
                "Spain",
                new BigDecimal(15),
                new BigDecimal(0),
                "2026-08-31",
                "",
                "",
                true,
                null,
                null,
                null,
                ""
        );
    }

    @Nested
    @DisplayName("getPromotionList Tests")
    class GetPromotionListTests {

        @Test
        @DisplayName("Should return list of all promotions")
        void shouldReturnListOfAllPromotions() {
            // Arrange
            List<PromotionResponse> promotionList = List.of(testPromotionResponse);
            when(promotionService.getPromotionList()).thenReturn(promotionList);

            // Act
            ResponseEntity<List<PromotionResponse>> response = promotionController.getPromotionList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Summer Vacation Special", response.getBody().get(0).title());
            verify(promotionService, times(1)).getPromotionList();
        }

        @Test
        @DisplayName("Should return empty list when no promotions exist")
        void shouldReturnEmptyListWhenNoPromotionsExist() {
            // Arrange
            when(promotionService.getPromotionList()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<PromotionResponse>> response = promotionController.getPromotionList();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(promotionService, times(1)).getPromotionList();
        }
    }

    @Nested
    @DisplayName("getPromotionListActive Tests")
    class GetPromotionListActiveTests {

        @Test
        @DisplayName("Should return list of active promotions")
        void shouldReturnListOfActivePromotions() {
            // Arrange
            List<PromotionResponse> activePromotions = List.of(testPromotionResponse);
            when(promotionService.getPromotionListActive()).thenReturn(activePromotions);

            // Act
            ResponseEntity<List<PromotionResponse>> response = promotionController.getPromotionListActive();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertTrue(response.getBody().get(0).isActive());
            verify(promotionService, times(1)).getPromotionListActive();
        }

        @Test
        @DisplayName("Should return empty list when no active promotions exist")
        void shouldReturnEmptyListWhenNoActivePromotionsExist() {
            // Arrange
            when(promotionService.getPromotionListActive()).thenReturn(new ArrayList<>());

            // Act
            ResponseEntity<List<PromotionResponse>> response = promotionController.getPromotionListActive();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isEmpty());
            verify(promotionService, times(1)).getPromotionListActive();
        }
    }

    @Nested
    @DisplayName("addPromotion Tests")
    class AddPromotionTests {

        @Test
        @DisplayName("Should add new promotion successfully")
        void shouldAddNewPromotionSuccessfully() {
            // Arrange
            when(promotionService.addPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(false)))
                    .thenReturn(testPromotionResponse);

            // Act
            ResponseEntity<PromotionResponse> response = promotionController.addPromotion(mockImage, testPromotionRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Summer Vacation Special", response.getBody().title());
            verify(promotionService, times(1)).addPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(false));
        }

        @Test
        @DisplayName("Should handle promotion with discount percentage")
        void shouldHandlePromotionWithDiscountPercentage() {
            // Arrange
            when(promotionService.addPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(false)))
                    .thenReturn(testPromotionResponse);

            // Act
            ResponseEntity<PromotionResponse> response = promotionController.addPromotion(mockImage, testPromotionRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());;
            verify(promotionService, times(1)).addPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(false));
        }
    }

    @Nested
    @DisplayName("deletePromotion Tests")
    class DeletePromotionTests {

        @Test
        @DisplayName("Should delete promotion successfully")
        void shouldDeletePromotionSuccessfully() {
            // Arrange
            when(promotionService.removePromotion(testPromotionId)).thenReturn("Promotion deleted successfully");

            // Act
            ResponseEntity<String> response = promotionController.deletePromotion(testPromotionId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().contains("deleted"));
            verify(promotionService, times(1)).removePromotion(testPromotionId);
        }

        @Test
        @DisplayName("Should handle non-existent promotion deletion")
        void shouldHandleNonExistentPromotionDeletion() {
            // Arrange
            UUID nonExistentId = UUID.randomUUID();
            when(promotionService.removePromotion(nonExistentId)).thenReturn("Promotion deleted successfully");

            // Act
            ResponseEntity<String> response = promotionController.deletePromotion(nonExistentId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(promotionService, times(1)).removePromotion(nonExistentId);
        }
    }

    @Nested
    @DisplayName("updatePromotion Tests")
    class UpdatePromotionTests {

        @Test
        @DisplayName("Should update existing promotion successfully")
        void shouldUpdateExistingPromotionSuccessfully() {
            // Arrange
            PromotionResponse updatedResponse = PromotionResponse.builder()
                    .id(testPromotionId)
                    .title("Updated Summer Special")
                    .description("Updated description")
                    .destination("Spain")
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .imageUrl("https://example.com/updated.jpg")
                    .isActive(true)
                    .build();

            when(promotionService.modifyPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(true)))
                    .thenReturn(updatedResponse);

            // Act
            ResponseEntity<PromotionResponse> response = promotionController.updatePromotion(mockImage, testPromotionRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(promotionService, times(1)).modifyPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(true));
        }

        @Test
        @DisplayName("Should handle null response when promotion not found")
        void shouldHandleNullResponseWhenPromotionNotFound() {
            // Arrange
            when(promotionService.modifyPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(true)))
                    .thenReturn(null);

            // Act
            ResponseEntity<PromotionResponse> response = promotionController.updatePromotion(mockImage, testPromotionRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNull(response.getBody());
            verify(promotionService, times(1)).modifyPromotion(any(MultipartFile.class), any(PromotionRequest.class), eq(true));
        }
    }

    @Nested
    @DisplayName("enableDisablePromotion Tests")
    class EnableDisablePromotionTests {

        @Test
        @DisplayName("Should enable/disable promotion successfully")
        void shouldEnableDisablePromotionSuccessfully() {
            // Arrange
            PromotionResponse inactiveResponse = PromotionResponse.builder()
                    .id(testPromotionId)
                    .title("Summer Vacation Special")
                    .description("Amazing summer vacation deals")
                    .destination("Spain")
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .imageUrl("https://example.com/image.jpg")
                    .isActive(false)
                    .build();

            when(promotionService.enableDisable(testPromotionId)).thenReturn(inactiveResponse);

            // Act
            ResponseEntity<PromotionResponse> response = promotionController.enableDisablePromotion(testPromotionId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isActive());
            verify(promotionService, times(1)).enableDisable(testPromotionId);
        }

        @Test
        @DisplayName("Should toggle promotion status")
        void shouldTogglePromotionStatus() {
            // Arrange
            when(promotionService.enableDisable(testPromotionId)).thenReturn(testPromotionResponse);

            // Act
            ResponseEntity<PromotionResponse> response = promotionController.enableDisablePromotion(testPromotionId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isActive());
            verify(promotionService, times(1)).enableDisable(testPromotionId);
        }
    }
}


package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.PromotionRequest;
import com.travelandrepeat.api.dto.PromotionResponse;
import com.travelandrepeat.api.entity.Promotion;
import com.travelandrepeat.api.repository.PromotionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PromotionService Tests")
class PromotionServiceTest {

    @Mock
    private PromotionRepo promotionRepo;

    @Mock
    private MultipartFile mockImage;

    @Mock
    private PromotionImageService promotionImageService;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    private UUID testPromotionId;
    private Promotion testPromotion;
    private PromotionRequest testPromotionRequest;

    @BeforeEach
    void setUp() {
        testPromotionId = UUID.randomUUID();

        testPromotion = new Promotion();
        testPromotion.setId(testPromotionId);
        testPromotion.setTitle("Summer Vacation Special");
        testPromotion.setDescription("Amazing summer vacation deals");
        testPromotion.setDestination("Spain");
        testPromotion.setStartDate(LocalDateTime.now());
        testPromotion.setEndDate(LocalDateTime.now());
        testPromotion.setImageUrl("https://example.com/image.jpg");
        testPromotion.setActive(true);

        testPromotionRequest = new PromotionRequest(
                testPromotionId,
                "Summer Vacation Special",
                "Amazing summer vacation deals",
                "Spain",
                new BigDecimal(0),
                new BigDecimal(0),
                "USD",
                "",
                "",
                true,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "imageUrl"
        );
    }

    @Nested
    @DisplayName("getPromotionList Tests")
    class GetPromotionListTests {

        @Test
        @DisplayName("Should return list of all promotions")
        void shouldReturnListOfAllPromotions() {
            // Arrange
            List<Promotion> promotionList = List.of(testPromotion);
            when(promotionRepo.findAll()).thenReturn(promotionList);

            // Act
            List<PromotionResponse> result = promotionService.getPromotionList();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(promotionRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no promotions exist")
        void shouldReturnEmptyListWhenNoPromotionsExist() {
            // Arrange
            when(promotionRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<PromotionResponse> result = promotionService.getPromotionList();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(promotionRepo, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getPromotionListActive Tests")
    class GetPromotionListActiveTests {

        @Test
        @DisplayName("Should return list of active promotions")
        void shouldReturnListOfActivePromotions() {
            // Arrange
            List<Promotion> activeList = List.of(testPromotion);
            when(promotionRepo.findAll()).thenReturn(activeList);

            // Act
            List<PromotionResponse> result = promotionService.getPromotionListActive();

            // Assert
            assertNotNull(result);
            // Filter only active promotions
            verify(promotionRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no active promotions exist")
        void shouldReturnEmptyListWhenNoActivePromotionsExist() {
            // Arrange
            when(promotionRepo.findAll()).thenReturn(new ArrayList<>());

            // Act
            List<PromotionResponse> result = promotionService.getPromotionListActive();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("addPromotion Tests")
    class AddPromotionTests {

        @Test
        @DisplayName("Should add new promotion successfully")
        void shouldAddNewPromotionSuccessfully() {
            // Arrange
            Promotion savedPromotion = new Promotion();
            savedPromotion.setId(UUID.randomUUID());
            savedPromotion.setTitle(testPromotionRequest.getTitle());
            savedPromotion.setActive(true);

            when(promotionRepo.save(any(Promotion.class))).thenReturn(savedPromotion);

            // Act
            PromotionResponse result = promotionService.addPromotion(mockImage, testPromotionRequest, false);

            // Assert
            assertNotNull(result);
            verify(promotionRepo, times(1)).save(any(Promotion.class));
        }

        @Test
        @DisplayName("Should set id to null when adding new promotion")
        void shouldSetIdToNullWhenAddingNewPromotion() {
            // Arrange
            Promotion savedPromotion = new Promotion();
            savedPromotion.setId(UUID.randomUUID());
            savedPromotion.setTitle("New Promotion");
            savedPromotion.setActive(true);

            when(promotionRepo.save(any(Promotion.class))).thenReturn(savedPromotion);

            // Act
            promotionService.addPromotion(mockImage, testPromotionRequest, false);

            // Assert
            verify(promotionRepo, times(1)).save(argThat(promo -> promo.getId() == null));
        }
    }

    @Nested
    @DisplayName("removePromotion Tests")
    class RemovePromotionTests {

        @Test
        @DisplayName("Should delete promotion successfully")
        void shouldDeletePromotionSuccessfully() {
            // Arrange
            when(promotionRepo.findById(testPromotionId)).thenReturn(Optional.of(testPromotion));
            when(promotionImageService.remove(anyString())).thenReturn("");
            doNothing().when(promotionRepo).deleteById(testPromotionId);

            // Act
            String result = promotionService.removePromotion(testPromotionId);

            // Assert
            assertNotNull(result);
            verify(promotionRepo, times(1)).deleteById(testPromotionId);
        }
    }

    @Nested
    @DisplayName("modifyPromotion Tests")
    class ModifyPromotionTests {

        @Test
        @DisplayName("Should update existing promotion successfully")
        void shouldUpdateExistingPromotionSuccessfully() {
            // Arrange
            Promotion updatedPromotion = new Promotion();
            updatedPromotion.setId(testPromotionId);
            updatedPromotion.setTitle("Updated Promotion");
            updatedPromotion.setImageUrl("imageUrl");

            when(promotionRepo.findById(updatedPromotion.getId())).thenReturn(Optional.of(updatedPromotion));
            when(promotionImageService.remove(updatedPromotion.getImageUrl())).thenReturn("");
            when(promotionRepo.save(any(Promotion.class))).thenReturn(updatedPromotion);

            // Act
            PromotionResponse result = promotionService.modifyPromotion(mockImage, testPromotionRequest, true);

            // Assert
            assertNotNull(result);
            verify(promotionRepo, times(1)).save(any(Promotion.class));
        }
    }

    @Nested
    @DisplayName("enableDisable Tests")
    class EnableDisableTests {

        @Test
        @DisplayName("Should toggle promotion active status")
        void shouldTogglePromotionActiveStatus() {
            // Arrange
            Promotion inactivePromotion = new Promotion();
            inactivePromotion.setId(testPromotionId);
            inactivePromotion.setTitle("Summer Vacation Special");
            inactivePromotion.setActive(false);

            when(promotionRepo.findById(testPromotionId)).thenReturn(Optional.of(testPromotion));
            when(promotionRepo.save(any(Promotion.class))).thenReturn(inactivePromotion);

            // Act
            PromotionResponse result = promotionService.enableDisable(testPromotionId);

            // Assert
            assertNotNull(result);
            verify(promotionRepo, times(1)).findById(testPromotionId);
            verify(promotionRepo, times(1)).save(any(Promotion.class));
        }

        @Test
        @DisplayName("Should return null when promotion not found")
        void shouldReturnNullWhenPromotionNotFound() {
            // Arrange
            when(promotionRepo.findById(testPromotionId)).thenReturn(Optional.empty());

            // Act
            PromotionResponse result = promotionService.enableDisable(testPromotionId);

            // Assert
            assertNull(result);
            verify(promotionRepo, times(1)).findById(testPromotionId);
            verify(promotionRepo, never()).save(any());
        }
    }
}


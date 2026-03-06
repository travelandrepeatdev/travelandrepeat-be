package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.DashboardStats;
import com.travelandrepeat.api.entity.Blog;
import com.travelandrepeat.api.entity.Promotion;
import com.travelandrepeat.api.repository.BlogRepo;
import com.travelandrepeat.api.repository.ClientRepo;
import com.travelandrepeat.api.repository.ProviderRepo;
import com.travelandrepeat.api.repository.PromotionRepo;
import com.travelandrepeat.api.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardService Tests")
class DashboardServiceTest {

    @Mock
    private ClientRepo clientRepo;

    @Mock
    private ProviderRepo providerRepo;

    @Mock
    private PromotionRepo promotionRepo;

    @Mock
    private BlogRepo blogRepo;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void setUp() {
        // ...existing code...
    }

    @Nested
    @DisplayName("getDashboardStats Tests")
    class GetDashboardStatsTests {

        @Test
        @DisplayName("Should return dashboard stats with correct counts")
        void shouldReturnDashboardStatsWithCorrectCounts() {
            // Arrange
            when(clientRepo.count()).thenReturn(45L);
            when(providerRepo.count()).thenReturn(12L);
            when(promotionRepo.findAll()).thenReturn(List.of(Promotion.builder().isActive(true).build()));
            when(blogRepo.findAll()).thenReturn(List.of(Blog.builder().blogStatus("Publicado").build()));

            // Act
            DashboardStats result = dashboardService.getDashboardStats();

            // Assert
            assertNotNull(result);
            assertEquals("45", result.totalClients());
            assertEquals("12", result.totalProviders());
            assertEquals("1", result.activePromotions());
            assertEquals("1", result.publishedBlogs());
            verify(clientRepo, times(1)).count();
            verify(providerRepo, times(1)).count();
            verify(promotionRepo, times(1)).findAll();
            verify(blogRepo, times(1)).findAll();
        }

    }
}


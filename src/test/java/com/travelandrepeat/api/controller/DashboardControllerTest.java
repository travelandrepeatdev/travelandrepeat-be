package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.DashboardStats;
import com.travelandrepeat.api.service.DashboardService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardController Tests")
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    private DashboardStats testStats;

    @BeforeEach
    void setUp() {
        testStats = DashboardStats.builder()
                .totalClients("45")
                .totalProviders("12")
                .activePromotions("8")
                .publishedBlogs("3")
                .build();
    }

    @Nested
    @DisplayName("getDashboardStats Tests")
    class GetDashboardStatsTests {

        @Test
        @DisplayName("Should return dashboard stats successfully")
        void shouldReturnDashboardStatsSuccessfully() {
            // Arrange
            when(dashboardService.getDashboardStats()).thenReturn(testStats);

            // Act
            ResponseEntity<DashboardStats> response = dashboardController.getDashboardStats();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("45", response.getBody().totalClients());
            assertEquals("12", response.getBody().totalProviders());
            assertEquals("8", response.getBody().activePromotions());
            assertEquals("3", response.getBody().publishedBlogs());
            verify(dashboardService, times(1)).getDashboardStats();
        }

        @Test
        @DisplayName("Should return dashboard stats with zero values")
        void shouldReturnDashboardStatsWithZeroValues() {
            // Arrange
            DashboardStats emptyStats = DashboardStats.builder()
                    .totalClients("0")
                    .totalProviders("0")
                    .totalProviders("0")
                    .activePromotions("0")
                    .build();
            when(dashboardService.getDashboardStats()).thenReturn(emptyStats);

            // Act
            ResponseEntity<DashboardStats> response = dashboardController.getDashboardStats();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("0", response.getBody().totalClients());
            assertEquals("0", response.getBody().totalProviders());
            verify(dashboardService, times(1)).getDashboardStats();
        }

        @Test
        @DisplayName("Should return dashboard stats with large values")
        void shouldReturnDashboardStatsWithLargeValues() {
            // Arrange
            DashboardStats largeStats = DashboardStats.builder()
                    .totalClients("1000")
                    .totalProviders("500")
                    .activePromotions("250")
                    .publishedBlogs("150")
                    .build();
            when(dashboardService.getDashboardStats()).thenReturn(largeStats);

            // Act
            ResponseEntity<DashboardStats> response = dashboardController.getDashboardStats();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("1000", response.getBody().totalClients());
            assertEquals("500", response.getBody().totalProviders());
            verify(dashboardService, times(1)).getDashboardStats();
        }

        @Test
        @DisplayName("Should return response with correct HTTP status")
        void shouldReturnResponseWithCorrectHttpStatus() {
            // Arrange
            when(dashboardService.getDashboardStats()).thenReturn(testStats);

            // Act
            ResponseEntity<DashboardStats> response = dashboardController.getDashboardStats();

            // Assert
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(dashboardService, times(1)).getDashboardStats();
        }
    }
}


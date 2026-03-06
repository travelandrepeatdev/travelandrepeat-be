package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.service.BanxicoService;
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
@DisplayName("DollarController Tests")
class DollarControllerTest {

    @Mock
    private BanxicoService banxicoService;

    @InjectMocks
    private DollarController dollarController;

    @BeforeEach
    void setUp() {
        // ...existing code...
    }

    @Nested
    @DisplayName("getDollarRate Tests")
    class GetDollarRateTests {

        @Test
        @DisplayName("Should return exchange rate successfully")
        void shouldReturnExchangeRateSuccessfully() {
            // Arrange
            String exchangeRate = "18.50";
            when(banxicoService.getExchangeRate()).thenReturn(exchangeRate);

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(exchangeRate, response.getBody());
            verify(banxicoService, times(1)).getExchangeRate();
        }

        @Test
        @DisplayName("Should return formatted exchange rate")
        void shouldReturnFormattedExchangeRate() {
            // Arrange
            String formattedRate = "18.5045";
            when(banxicoService.getExchangeRate()).thenReturn(formattedRate);

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("."));
            verify(banxicoService, times(1)).getExchangeRate();
        }

        @Test
        @DisplayName("Should return default rate when service fails")
        void shouldReturnDefaultRateWhenServiceFails() {
            // Arrange
            String defaultRate = "--.--";
            when(banxicoService.getExchangeRate()).thenReturn(defaultRate);

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(defaultRate, response.getBody());
            verify(banxicoService, times(1)).getExchangeRate();
        }

        @Test
        @DisplayName("Should return response with OK status")
        void shouldReturnResponseWithOkStatus() {
            // Arrange
            when(banxicoService.getExchangeRate()).thenReturn("17.95");

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertTrue(response.getStatusCode().is2xxSuccessful());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(banxicoService, times(1)).getExchangeRate();
        }

        @Test
        @DisplayName("Should return numeric exchange rate value")
        void shouldReturnNumericExchangeRateValue() {
            // Arrange
            String numericRate = "19.25";
            when(banxicoService.getExchangeRate()).thenReturn(numericRate);

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertNotNull(response.getBody());
            assertTrue(response.getBody().matches("^\\d+\\.\\d{2}$|^--\\.--$"));
            verify(banxicoService, times(1)).getExchangeRate();
        }

        @Test
        @DisplayName("Should handle high exchange rate values")
        void shouldHandleHighExchangeRateValues() {
            // Arrange
            String highRate = "25.75";
            when(banxicoService.getExchangeRate()).thenReturn(highRate);

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(highRate, response.getBody());
        }

        @Test
        @DisplayName("Should handle low exchange rate values")
        void shouldHandleLowExchangeRateValues() {
            // Arrange
            String lowRate = "16.50";
            when(banxicoService.getExchangeRate()).thenReturn(lowRate);

            // Act
            ResponseEntity<String> response = dollarController.getDollarRate();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(lowRate, response.getBody());
            verify(banxicoService, times(1)).getExchangeRate();
        }
    }
}


package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.QuotationFormRequest;
import com.travelandrepeat.api.service.CaptchaValidatorService;
import com.travelandrepeat.api.service.MailService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MailController Tests")
class MailControllerTest {

    @Mock
    private MailService mailService;

    @Mock
    private CaptchaValidatorService captchaValidatorService;

    @InjectMocks
    private MailController mailController;

    private QuotationFormRequest testQuotationRequest;

    @BeforeEach
    void setUp() {
        testQuotationRequest = new QuotationFormRequest(
                "Juan Pérez",
                "juan@example.com",
                "+34 666 123 456",
                "Madrid, Spain",
                "Barcelona",
                "2026-07-01",
                "2026-07-10",
                false,
                true,
                true,
                "2000-5000 EUR",
                "Luxury",
                "Beach and Culture",
                2,
                2,
                0,
                "",
                false,
                "Relaxation",
                "Tropical Beach",
                "Some additional comments",
                "recaptcha-token-123"
        );
    }

    @Nested
    @DisplayName("sendQuotationForm Tests")
    class SendQuotationFormTests {

        @Test
        @DisplayName("Should send quotation form successfully")
        void shouldSendQuotationFormSuccessfully() throws Exception {
            // Arrange
            doNothing().when(captchaValidatorService).verify(any());
            doNothing().when(mailService).sendMail(any(QuotationFormRequest.class));

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(testQuotationRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().contains("successfully"));
            verify(captchaValidatorService, times(1)).verify(any());
            verify(mailService, times(1)).sendMail(any(QuotationFormRequest.class));
        }

        @Test
        @DisplayName("Should return FORBIDDEN when reCAPTCHA verification fails")
        void shouldReturnForbiddenWhenRecaptchaVerificationFails() throws Exception {
            // Arrange
            doThrow(new Exception("reCAPTCHA verification failed"))
                    .when(captchaValidatorService).verify(any());

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(testQuotationRequest);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(captchaValidatorService, times(1)).verify(any());
            verify(mailService, never()).sendMail(any());
        }

        @Test
        @DisplayName("Should return INTERNAL_SERVER_ERROR when mail sending fails")
        void shouldReturnInternalServerErrorWhenMailSendingFails() throws Exception {
            // Arrange
            doNothing().when(captchaValidatorService).verify(any());
            doThrow(new RuntimeException("Mail server error"))
                    .when(mailService).sendMail(any(QuotationFormRequest.class));

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(testQuotationRequest);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNotNull(response.getBody());
            verify(captchaValidatorService, times(1)).verify(any());
            verify(mailService, times(1)).sendMail(any());
        }

        @Test
        @DisplayName("Should handle quotation with complete information")
        void shouldHandleQuotationWithCompleteInformation() throws Exception {
            // Arrange
            doNothing().when(captchaValidatorService).verify(any());
            doNothing().when(mailService).sendMail(any(QuotationFormRequest.class));

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(testQuotationRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Mail sent successfully!", response.getBody());
            verify(mailService, times(1)).sendMail(any(QuotationFormRequest.class));
        }

        @Test
        @DisplayName("Should handle quotation with minimal information")
        void shouldHandleQuotationWithMinimalInformation() throws Exception {
            // Arrange
            QuotationFormRequest minimalRequest = new QuotationFormRequest(
                    "John Doe",
                    "john@example.com",
                    "+1 234 567 8900",
                    "",
                    "New York",
                    "2026-08-01",
                    "2026-08-10",
                    false,
                    null,
                    null,
                    "",
                    "Budget",
                    "City Tour",
                    1,
                    1,
                    0,
                    "",
                    false,
                    "",
                    "",
                    "No additional comments",
                    "recaptcha-token-456"
            );

            doNothing().when(captchaValidatorService).verify(any());
            doNothing().when(mailService).sendMail(any(QuotationFormRequest.class));

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(minimalRequest);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(mailService, times(1)).sendMail(any(QuotationFormRequest.class));
        }

        @Test
        @DisplayName("Should propagate reCAPTCHA error message")
        void shouldPropagateRecaptchaErrorMessage() throws Exception {
            // Arrange
            String errorMessage = "Invalid reCAPTCHA token";
            doThrow(new Exception(errorMessage))
                    .when(captchaValidatorService).verify(any());

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(testQuotationRequest);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertTrue(response.getBody().contains(errorMessage));
        }

        @Test
        @DisplayName("Should propagate mail service error message")
        void shouldPropagateMailServiceErrorMessage() throws Exception {
            // Arrange
            String errorMessage = "Failed to connect to mail server";
            doNothing().when(captchaValidatorService).verify(any());
            doThrow(new RuntimeException(errorMessage))
                    .when(mailService).sendMail(any(QuotationFormRequest.class));

            // Act
            ResponseEntity<String> response = mailController.sendQuotationForm(testQuotationRequest);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertTrue(response.getBody().contains(errorMessage));
        }
    }
}


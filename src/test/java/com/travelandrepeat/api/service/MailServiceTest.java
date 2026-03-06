package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.QuotationFormRequest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MailService Tests")
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailServiceImpl mailService;

    private QuotationFormRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new QuotationFormRequest(
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
    @DisplayName("sendMail Tests")
    class SendMailTests {

        @Test
        @DisplayName("Should send mail successfully")
        void shouldSendMailSuccessfully() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mailSender, times(1)).send(any(MimeMessage.class));
            });
        }

        @Test
        @DisplayName("Should set mail subject with client name")
        void shouldSetMailSubjectWithClientName() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mimeMessage, times(1)).setSubject(contains(testRequest.completeName()));
            });
        }

        @Test
        @DisplayName("Should set mail content as HTML")
        void shouldSetMailContentAsHtml() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mimeMessage, times(1)).setContent(anyString(), eq("text/html; charset=utf-8"));
            });
        }

        @Test
        @DisplayName("Should handle complete quotation form")
        void shouldHandleCompleteQuotationForm() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mailSender, times(1)).send(any(MimeMessage.class));
            });
        }

        @Test
        @DisplayName("Should include client information in email")
        void shouldIncludeClientInformationInEmail() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mimeMessage, times(1)).setContent(contains(testRequest.completeName()), anyString());
            });
        }

        @Test
        @DisplayName("Should include trip information in email")
        void shouldIncludeTripInformationInEmail() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mimeMessage, times(1)).setContent(contains(testRequest.destiny()), anyString());
            });
        }

        @Test
        @DisplayName("Should include budget information in email")
        void shouldIncludeBudgetInformationInEmail() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mimeMessage, times(1)).setContent(contains(testRequest.budget()), anyString());
            });
        }

        @Test
        @DisplayName("Should handle quotation with travelers information")
        void shouldHandleQuotationWithTravelersInformation() {
            // Arrange
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(testRequest);
                verify(mimeMessage).setContent(contains("2"), anyString());
            });
        }

        @Test
        @DisplayName("Should throw exception when mail sending fails")
        void shouldThrowExceptionWhenMailSendingFails() {
            // Arrange
            doThrow(new RuntimeException("Mail server error"))
                    .when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                mailService.sendMail(testRequest);
            });
        }

        @Test
        @DisplayName("Should handle minimal quotation form")
        void shouldHandleMinimalQuotationForm() {
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
                    "No comments",
                    "token"
            );
            doNothing().when(mailSender).send(any(MimeMessage.class));

            // Act & Assert
            assertDoesNotThrow(() -> {
                mailService.sendMail(minimalRequest);
                verify(mailSender, times(1)).send(any(MimeMessage.class));
            });
        }
    }
}


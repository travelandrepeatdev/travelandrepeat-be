package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.RecaptchaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CaptchaValidatorService Tests")
class CaptchaValidatorServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CaptchaValidatorServiceImpl captchaValidatorService;

    private String testToken;

    @BeforeEach
    void setUp() {
        testToken = "valid-recaptcha-token-123";
        ReflectionTestUtils.setField(captchaValidatorService, "verifyUrl", "url");
        ReflectionTestUtils.setField(captchaValidatorService, "secret", "secret");
        ReflectionTestUtils.setField(captchaValidatorService, "restTemplate", restTemplate);
    }

    @Nested
    @DisplayName("verify Tests")
    class VerifyTests {

        @Test
        @DisplayName("Should verify reCAPTCHA token successfully")
        void shouldVerifyRecaptchaTokenSuccessfully() {
            // Arrange
            RecaptchaResponse successResponse = new RecaptchaResponse(
                    true,
                    "2026-03-05T10:30:00Z",
                    "localhost",
                    0.95,
                    "login",
                    null
            );
            when(restTemplate.postForObject(anyString(), any(MultiValueMap.class), any())).thenReturn(successResponse);

            // Act & Assert
            assertDoesNotThrow(() -> captchaValidatorService.verify(testToken));
        }
    }
}


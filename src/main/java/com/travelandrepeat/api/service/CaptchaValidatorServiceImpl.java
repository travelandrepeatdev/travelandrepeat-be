package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.RecaptchaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
@SuppressWarnings("unused")
public class CaptchaValidatorServiceImpl implements CaptchaValidatorService {

    @Value("${google.recaptcha.secret}")
    private String secret;

    @Value("${google.recaptcha.verify-url}")
    private String verifyUrl;

    public void verify(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", secret);
        body.add("response", token);

        RecaptchaResponse recaptchaResponse;
        // TODO: Handle exceptions properly
        try {
            recaptchaResponse = restTemplate.postForObject(verifyUrl, body, RecaptchaResponse.class);
        } catch (Exception e) {
            log.error("Error verifying reCAPTCHA", e);
            throw new Exception("Error verifying reCAPTCHA", e);
        }

        if (recaptchaResponse != null) {
            log.error("reCAPTCHA from {} was solved.", recaptchaResponse.hostname());
            if (!recaptchaResponse.success()) {
                log.error("reCAPTCHA verification failed: {}", recaptchaResponse.errorCodes());
                throw new Exception("reCAPTCHA invalid");
            }
            if (recaptchaResponse.score() < 0.5) {
                log.error("reCAPTCHA score too low: {}", recaptchaResponse.score());
                throw new Exception("Suspect activity detected");
            }
        } else {
            log.error("Empty reCAPTCHA response");
            throw new Exception("Empty reCAPTCHA response");
        }
    }
}

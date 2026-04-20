package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.QuotationFormRequest;
import com.travelandrepeat.api.service.CaptchaValidatorService;
import com.travelandrepeat.api.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;
    private final CaptchaValidatorService captchaValidatorService;

    @PostMapping(path = "/sendQuotationForm")
    public ResponseEntity<String> sendQuotationForm(@Valid @RequestBody QuotationFormRequest request) {
        // TODO: Handle exceptions properly
        try {
            captchaValidatorService.verify(request.recaptchaToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

        try {
            mailService.sendMail(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Mail sent successfully!");
    }
}

package com.travelandrepeat.api.controller;

import com.travelandrepeat.api.dto.QuotationFormRequest;
import com.travelandrepeat.api.service.CaptchaValidatorService;
import com.travelandrepeat.api.service.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private CaptchaValidatorService captchaValidatorService;

    @PostMapping(
            path = "/sendQuotationForm",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
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
        return ResponseEntity.status(HttpStatus.OK).body("Mail sent successfully!");
    }
}

package com.travelandrepeat.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public record QuotationFormRequest(

        /* Client Information */
        @NotBlank
        String completeName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String phone,
        String countryCity,

        /* Trip Information */
        @NotBlank
        String destiny,
        @NotBlank
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String outDate,
        @NotBlank
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String returnDate,
        boolean areDatesFlexible,

        /* Budget and Priorities */
        String budget,
        @NotBlank
        String levelType,
        @NotBlank
        String priority,

        /* Travelers Information */
        int totalTravelers,
        int totalAdults,
        int totalMinors,
        String minorsAges,
        boolean areBabiesTraveling,

        /* Trip Preferences */
        @NotBlank
        String tripType,
        @NotBlank
        String tripTheme,

        /* Additional Comments */
        String comments,

        @NotBlank
        String recaptchaToken
){}
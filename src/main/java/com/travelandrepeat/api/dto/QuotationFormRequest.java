package com.travelandrepeat.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
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
        Boolean haveVisa,
        Boolean havePassport,

        /* Budget and Priorities */
        String budget,
        @NotBlank
        String levelType,
        @NotBlank
        String priority,

        /* Travelers Information */
        Integer totalTravelers,
        Integer totalAdults,
        Integer totalMinors,
        String minorsAges,
        boolean areBabiesTraveling,

        /* Trip Preferences */
        @NotBlank
        String tripType,
        @NotBlank
        String tripTheme,

        /* Additional Comments */
        String comments,
        String recaptchaToken
){
    @NonNull
    @Override
    public String toString() {
        return "QuotationFormRequest = {" +
                " completeName: " + completeName +
                " email: " + email +
                " phone: " + phone +
                " countryCity: " + countryCity +
                " destiny: " + destiny +
                " outDate: " + outDate +
                " returnDate: " + returnDate +
                " areDatesFlexible: " + areDatesFlexible +
                " budget: " + budget +
                " levelType: " + levelType +
                " priority: " + priority +
                " totalTravelers: " + totalTravelers +
                " totalAdults: " + totalAdults +
                " totalMinors: " + totalMinors +
                " minorsAges: " + minorsAges +
                " areBabiesTraveling: " + areBabiesTraveling +
                " tripType: " + tripType +
                " tripTheme: " + tripTheme +
                " comments: " + comments +
                " recaptchaToken: " + recaptchaToken +
                "}";
    }
}
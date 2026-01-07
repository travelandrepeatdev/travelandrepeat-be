package com.travelandrepeat.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecaptchaResponse (
    boolean success,
    @JsonProperty("challenge_ts")
    String challengeTs,
    String hostname,
    Double score,
    String action,
    @JsonProperty("error-codes")
    List<String> errorCodes
) {}

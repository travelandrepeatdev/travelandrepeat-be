package com.travelandrepeat.api.dto;

public record LoginRequest(
    String email,
    String password
) {
}

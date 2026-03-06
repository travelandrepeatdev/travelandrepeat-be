package com.travelandrepeat.api.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ClientRequest (
        UUID id,
        String name,
        String email,
        String phone,
        String countryCode,
        String address,
        String notes,
        UUID createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

package com.travelandrepeat.api.dto;

import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProviderResponse(
        UUID id,
        String name,
        String contactName,
        String email,
        String phone,
        String category,
        String website,
        String notes,
        UUID createdBy
) {
}

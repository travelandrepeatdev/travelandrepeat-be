package com.travelandrepeat.api.dto;

import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ClientResponse(
    String id,
    String name,
    String email,
    String phone,
    String countryCode,
    String address,
    String notes,
    String createdBy,
    String createdAt,
    String updatedAt
) {
}

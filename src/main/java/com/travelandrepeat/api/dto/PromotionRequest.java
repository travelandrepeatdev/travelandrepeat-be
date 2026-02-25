package com.travelandrepeat.api.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PromotionRequest(
        UUID id,
        String title,
        String description,
        String destination,
        BigDecimal originalPrice,
        BigDecimal promoPrice,
        String currency,
        String imageUrl,
        String startDate,
        String endDate,
        boolean isActive,
        UUID createdBy,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
}

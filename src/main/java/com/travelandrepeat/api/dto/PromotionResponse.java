package com.travelandrepeat.api.dto;

import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PromotionResponse(
        UUID id,
        String title,
        String description,
        String destination,
        BigDecimal originalPrice,
        BigDecimal promoPrice,
        String currency,
        String imageUrl,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isActive,
        UUID createdBy,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
}

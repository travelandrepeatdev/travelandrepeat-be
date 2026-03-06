package com.travelandrepeat.api.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BlogRequest(
        UUID id,
        String title,
        String slug,
        String content,
        String excerpt,
        String coverImageUrl,
        String status,
        LocalDateTime publishedAt,
        UUID createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

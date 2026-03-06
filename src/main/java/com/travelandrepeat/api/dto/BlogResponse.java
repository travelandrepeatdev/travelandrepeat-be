package com.travelandrepeat.api.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BlogResponse(
        UUID id,
        String title,
        String slug,
        String content,
        String excerpt,
        String coverImageUrl,
        String status,
        UUID createdBy
) {
}

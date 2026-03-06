package com.travelandrepeat.api.dto;

import lombok.Builder;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse(
        UUID userId,
        String email,
        Boolean isActive,
        LocalDateTime lastLogin,
        String displayName,
        String avatarUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String role
) {}

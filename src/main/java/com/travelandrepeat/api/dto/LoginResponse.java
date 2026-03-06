package com.travelandrepeat.api.dto;

import java.util.List;
import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String email,
        String name,
        Boolean isActive,
        String avatarUrl,
        String accessToken,
        String role,
        List<String> permissions
) {
}

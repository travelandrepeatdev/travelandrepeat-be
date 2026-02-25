package com.travelandrepeat.api.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UserLogged(
        UUID userId,
        String email,
        String displayName,
        String avatarUrl,
        Boolean isActive,
        List<String> roles
) {
}

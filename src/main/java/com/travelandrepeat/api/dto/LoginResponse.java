package com.travelandrepeat.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UUID userId;
    private String email;
    private String name;
    private Boolean isActive;
    private String avatarUrl;
    private String role;
    private List<String> permissions;
}

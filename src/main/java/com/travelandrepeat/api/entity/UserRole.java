package com.travelandrepeat.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRole {

    @Id
    @Column(nullable = false, unique = true, name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true, name = "role_id")
    private UUID roleId;

    @Column(nullable = false, name = "assigned_at")
    private LocalDateTime assignedAt;
}

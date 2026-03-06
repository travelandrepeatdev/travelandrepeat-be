package com.travelandrepeat.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@Entity
@Data
@Table(name = "role_permissions")
@IdClass(RolePermissionId.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RolePermission {

    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @Id
    @Column(name = "permission_id")
    private UUID permissionId;
}

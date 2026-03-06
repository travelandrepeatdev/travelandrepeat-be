package com.travelandrepeat.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionId {
    private UUID roleId;
    private UUID permissionId;
}

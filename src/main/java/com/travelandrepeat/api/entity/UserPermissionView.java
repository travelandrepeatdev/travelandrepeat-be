package com.travelandrepeat.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Entity
@Immutable
@Getter
@Table(name = "user_permissions")
public class UserPermissionView {

    @Id
    @Column(unique = true, name = "permission_id")
    private UUID permissionId;

    @Column(unique = true, name = "user_id")
    private UUID userId;

    @Column(name = "permission_name")
    private String permissionName;
}

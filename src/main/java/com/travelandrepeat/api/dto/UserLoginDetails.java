package com.travelandrepeat.api.dto;

import com.travelandrepeat.api.entity.Permission;
import com.travelandrepeat.api.entity.Role;
import com.travelandrepeat.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.travelandrepeat.api.dto.Role.*;

@Data
@AllArgsConstructor
public class UserLoginDetails implements UserDetails {

    private UserLogged user;

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        user.roles().forEach(role -> authorities.add(
                new SimpleGrantedAuthority(
                        role.equalsIgnoreCase(ADMIN.name())
                                || role.equalsIgnoreCase(AGENT.name())
                                || role.equalsIgnoreCase(AUDIT.name())
                                || role.equalsIgnoreCase(VIEWER.name())
                                ? "ROLE_" + role
                                : role)
                )
        );
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public @NonNull String getUsername() {
        return user.displayName();
    }

    public static List<String> extractRoleListFromUser(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }

    public static List<String> extractPermissionListFromUser(User user) {
        return user.getRoles().stream()
                .flatMap(role -> {
                    List<Permission> permissions = role.getPermissions();
                    return permissions.stream().map(Permission::getName);
                })
                .toList();
    }
}

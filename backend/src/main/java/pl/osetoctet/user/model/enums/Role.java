package pl.osetoctet.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.osetoctet.user.model.enums.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    USER_CREATE,
                    USER_READ,
                    USER_UPDATE,
                    USER_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    USER_CREATE,
                    USER_READ,
                    USER_UPDATE,
                    USER_DELETE,
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }

}

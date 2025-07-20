package pl.osetoctet.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.osetoctet.common.model.entity.BaseEntity;
import pl.osetoctet.user.model.enums.Role;

import java.util.Collection;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 1024)
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role = Role.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

}

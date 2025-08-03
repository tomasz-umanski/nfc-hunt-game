package pl.osetoctet.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.osetoctet.user.model.dto.RegisterUserDto;
import pl.osetoctet.user.model.entity.User;
import pl.osetoctet.user.model.enums.Role;

import java.time.OffsetDateTime;

@Component
class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createFromRegisterDto(RegisterUserDto dto) {
        OffsetDateTime now = OffsetDateTime.now();

        return User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .ageConfirmedAt(now)
                .gameRulesAcceptedAt(now)
                .privacyPolicyAcceptedAt(now)
                .build();
    }

}

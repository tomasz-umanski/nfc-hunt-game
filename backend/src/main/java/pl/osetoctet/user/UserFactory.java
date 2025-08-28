package pl.osetoctet.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.osetoctet.tagaccess.model.entity.TagAccess;
import pl.osetoctet.user.model.dto.RegisterUserDto;
import pl.osetoctet.user.model.dto.UserAccessedTagDto;
import pl.osetoctet.user.model.dto.UserSummaryResponseDto;
import pl.osetoctet.user.model.entity.User;
import pl.osetoctet.user.model.enums.Role;

import java.time.OffsetDateTime;
import java.util.List;

@Component
class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static UserSummaryResponseDto createSummaryFromEntity(User user) {
        List<UserAccessedTagDto> userAccessedTagDtoList = user.getTagAccesses().stream()
                .map(UserFactory::createAccessedTagFromEntity)
                .toList();

        return UserSummaryResponseDto.builder()
                .email(user.getEmail())
                .createdAt(user.getCreationTimestamp())
                .userAccessedTagDtoList(userAccessedTagDtoList)
                .accessedTagsCount(userAccessedTagDtoList.size())
                .build();
    }

    private static UserAccessedTagDto createAccessedTagFromEntity(TagAccess tagAccess) {
        return UserAccessedTagDto.builder()
                .name(tagAccess.getTagLocation().getName())
                .longitude(tagAccess.getLongitude())
                .latitude(tagAccess.getLatitude())
                .unlockedAt(tagAccess.getCreationTimestamp())
                .build();
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

package pl.osetoctet.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.osetoctet.common.model.entity.BaseEntity;
import pl.osetoctet.user.model.enums.TokenScheme;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(name = "token", nullable = false, unique = true, length = 512)
    private String token;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "token_scheme", nullable = false, length = 20)
    private TokenScheme tokenScheme = TokenScheme.BEARER;

    @Column(nullable = false)
    private OffsetDateTime expirationDate;

    @Builder.Default
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "device_id", nullable = false, length = 512)
    private String deviceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}

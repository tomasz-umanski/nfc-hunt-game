package pl.osetoctet.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.osetoctet.user.model.entity.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface TokenRepository extends JpaRepository<Token, UUID> {

    /**
     * Retrieves token entity based on token value
     *
     * @param token the token value
     * @return optional token entity
     */
    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token t inner join User u
            on t.user.id = u.id
            where u.id = :id and (t.revoked = false) and t.deviceId = :deviceId
            """)
    List<Token> findAllValidTokenByUserAndDeviceId(UUID id, String deviceId);

    @Query(value = """
            select t from Token t inner join User u
            on t.user.id = u.id
            where u.id = :id and (t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(UUID id);

}

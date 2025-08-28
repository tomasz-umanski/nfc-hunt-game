package pl.osetoctet.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.osetoctet.user.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Retrieves user entity based on email
     *
     * @param email provided email
     * @return optional user entity
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Checks if user exist based on email
     *
     * @param email provided email
     * @return boolean representing user existence
     */
    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tagAccesses ta LEFT JOIN FETCH ta.tagLocation")
    List<User> findAllWithTagAccesses();

}

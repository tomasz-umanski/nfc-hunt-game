package pl.osetoctet.user;

import pl.osetoctet.user.model.entity.User;

import java.util.Optional;

public interface UserEntityService {

    Optional<User> findUserByEmail(String username);

}

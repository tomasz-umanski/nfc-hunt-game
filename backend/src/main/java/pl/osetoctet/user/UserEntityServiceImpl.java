package pl.osetoctet.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.osetoctet.user.model.entity.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserEntityServiceImpl implements UserEntityService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserByEmail(String username) {
        return userRepository.findUserByEmail(username);
    }

}

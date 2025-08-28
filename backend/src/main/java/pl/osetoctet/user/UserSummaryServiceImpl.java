package pl.osetoctet.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.osetoctet.user.model.dto.UserSummaryResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserSummaryServiceImpl implements UserSummaryService {

    private final UserRepository userRepository;

    @Override
    public List<UserSummaryResponseDto> getAll() {
        return userRepository.findAllWithTagAccesses().stream()
                .map(UserFactory::createSummaryFromEntity)
                .toList();
    }

}

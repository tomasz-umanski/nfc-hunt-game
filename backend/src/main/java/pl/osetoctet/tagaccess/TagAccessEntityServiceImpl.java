package pl.osetoctet.tagaccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class TagAccessEntityServiceImpl implements TagAccessEntityService {

    private final TagAccessRepository tagAccessRepository;

    @Override
    public boolean isAccessedByAnyUser(UUID tagLocationId) {
        return tagAccessRepository.existsByTagLocationId(tagLocationId);
    }

}

package pl.osetoctet.taglocation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.osetoctet.taglocation.model.entity.TagLocation;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class TagLocationEntityServiceImpl implements TagLocationEntityService {

    private final TagLocationRepository tagLocationRepository;

    @Override
    public List<TagLocation> findAll() {
        return tagLocationRepository.findAll();
    }

    @Override
    public Optional<TagLocation> findByNfcTagId(String nfcTagId) {
        return tagLocationRepository.findByNfcTagUuid(nfcTagId);
    }

}

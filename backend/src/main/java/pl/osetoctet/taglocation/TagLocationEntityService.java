package pl.osetoctet.taglocation;

import pl.osetoctet.taglocation.model.entity.TagLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagLocationEntityService {

    List<TagLocation> findAll();

    Optional<TagLocation> findByNfcTagId(String id);

    Optional<TagLocation> findById(UUID id);

}

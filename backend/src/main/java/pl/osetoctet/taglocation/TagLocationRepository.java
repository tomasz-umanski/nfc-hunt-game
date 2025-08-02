package pl.osetoctet.taglocation;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.osetoctet.taglocation.model.entity.TagLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface TagLocationRepository extends JpaRepository<TagLocation, UUID> {

    Optional<TagLocation> findActiveById(UUID tagId);

    List<TagLocation> findAllByOrderByNameAsc();

    boolean existsByNfcTagUuid(String nfcTagUuid);

    Optional<TagLocation> findByNfcTagUuid(String nfcTagUuid);

}

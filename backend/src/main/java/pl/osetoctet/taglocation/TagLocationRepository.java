package pl.osetoctet.taglocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.osetoctet.taglocation.model.entity.TagLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface TagLocationRepository extends JpaRepository<TagLocation, UUID> {

    @Query("SELECT DISTINCT tl FROM TagLocation tl LEFT JOIN FETCH tl.unlockImages ORDER BY tl.name ASC")
    List<TagLocation> findAllWithUnlockImagesOrderByNameAsc();

    @Query("SELECT tl FROM TagLocation tl LEFT JOIN FETCH tl.unlockImages WHERE tl.id = :id")
    Optional<TagLocation> findActiveByIdWithUnlockImages(@Param("id") UUID id);

    boolean existsByNfcTagUuid(String nfcTagUuid);

    Optional<TagLocation> findByNfcTagUuid(String nfcTagUuid);

}

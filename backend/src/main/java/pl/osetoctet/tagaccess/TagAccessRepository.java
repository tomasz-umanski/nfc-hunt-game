package pl.osetoctet.tagaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.osetoctet.tagaccess.model.entity.TagAccess;

import java.util.Set;
import java.util.UUID;

interface TagAccessRepository extends JpaRepository<TagAccess, UUID> {

    boolean existsByTagLocationId(UUID tagLocationId);

    boolean existsByUserIdAndTagLocationId(UUID userId, UUID tagLocationId);

    Set<TagAccess> findByUserId(UUID userId);

}

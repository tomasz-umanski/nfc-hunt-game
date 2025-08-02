package pl.osetoctet.taglocation.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.osetoctet.common.model.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tag_locations")
public class TagLocation extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "nfc_tag_uuid", nullable = false, unique = true, length = 512)
    private String nfcTagUuid;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "locked_image_filename", nullable = false, length = 64)
    private String lockedImageFilename;

    @Column(name = "unlocked_image_filename", nullable = false, length = 64)
    private String unlockedImageFilename;

    @OneToMany(mappedBy = "tagLocation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("displayOrder ASC")
    private List<TagLocationUnlockImage> unlockImages;

}

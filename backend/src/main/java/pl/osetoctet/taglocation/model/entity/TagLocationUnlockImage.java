package pl.osetoctet.taglocation.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.osetoctet.common.model.entity.BaseEntity;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tag_location_unlock_images")
public class TagLocationUnlockImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_location_id", nullable = false)
    private TagLocation tagLocation;

    @Column(name = "image_filename", nullable = false, length = 64)
    private String imageFilename;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

}

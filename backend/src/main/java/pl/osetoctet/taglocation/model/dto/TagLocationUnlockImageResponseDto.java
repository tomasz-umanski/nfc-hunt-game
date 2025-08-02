package pl.osetoctet.taglocation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data Transfer Object for unlock images associated with a tag location response")
public class TagLocationUnlockImageResponseDto {

    @Schema(description = "Filename of the image that can be unlocked at this tag location", example = "treasure-map-01.jpg")
    private String imageFilename;

    @Schema(description = "Display order of the unlock image (determines the sequence in which images are shown)", example = "1")
    private Integer displayOrder;

}

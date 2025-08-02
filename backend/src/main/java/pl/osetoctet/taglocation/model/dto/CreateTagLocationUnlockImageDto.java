package pl.osetoctet.taglocation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data Transfer Object for creating unlock images associated with a tag location")
public class CreateTagLocationUnlockImageDto {

    @NotBlank(message = "validation.unlockImage.imageFilename.required")
    @Size(max = 64, message = "validation.unlockImage.imageFilename.size")
    @Schema(description = "Filename of the image that can be unlocked at this tag location", example = "treasure-map-01.jpg")
    private String imageFilename;

    @NotNull(message = "validation.unlockImage.displayOrder.required")
    @Min(value = 1, message = "validation.unlockImage.displayOrder.min")
    @Max(value = 5, message = "validation.unlockImage.displayOrder.max")
    @Schema(description = "Display order of the unlock image (determines the sequence in which images are shown)", example = "1")
    private Integer displayOrder;

}

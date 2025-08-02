package pl.osetoctet.tagaccess.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Data Transfer Object for tag access details response")
public class TagAccessResponseDetailsDto {

    @Schema(description = "Name of the tag location", example = "Main Office Entrance")
    private String name;

    @Schema(description = "Detailed description of the tag location", example = "NFC tag located at the main entrance of the building, next to the reception desk")
    private String description;

    @Schema(description = "Filename of the image displayed when the tag location is in unlocked state", example = "760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String unlockedImageFilename;

    @Schema(description = "List of images that can be unlocked at this tag location")
    private List<TagAccessUnlockImageResponseDto> unlockImages;

}

package pl.osetoctet.taglocation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Schema(description = "Data Transfer Object for tag location response")
public class TagLocationResponseDto {

    @Schema(description = "Unique identifier of the tag location", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Name of the tag location", example = "Main Office Entrance")
    private String name;

    @Schema(description = "Detailed description of the tag location", example = "NFC tag located at the main entrance of the building, next to the reception desk")
    private String description;

    @Schema(description = "Latitude coordinate of the tag location", example = "51.12345678")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate of the tag location", example = "17.12345678")
    private BigDecimal longitude;

    @Schema(description = "Filename of the image displayed when the tag location is in locked state", example = "760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String lockedImageFilename;

    @Schema(description = "Filename of the image displayed when the tag location is in unlocked state", example = "760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String unlockedImageFilename;

    @Schema(description = "List of images that can be unlocked at this tag location")
    private List<TagLocationUnlockImageResponseDto> unlockImages;

}
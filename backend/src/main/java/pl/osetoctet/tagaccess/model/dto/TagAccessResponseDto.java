package pl.osetoctet.tagaccess.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Data Transfer Object for tag access response")
public class TagAccessResponseDto {

    @Schema(description = "Unique identifier of the tag location", example = "123e4567-e89b-12d3-a456-426614174000")
    private String tagLocationId;

    @Schema(description = "Latitude coordinate of the tag location", example = "51.12345678")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate of the tag location", example = "17.12345678")
    private BigDecimal longitude;

    @Schema(description = "Determines if user unlocked location", example = "true")
    private boolean isUnlocked;

    @Schema(description = "Filename of the image displayed when the tag location is in locked state", example = "760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String lockedImageFilename;

    @Schema(description = "Details of unlocked tag")
    private TagAccessResponseDetailsDto tagAccessResponseDetails;

}

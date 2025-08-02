package pl.osetoctet.taglocation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Schema(description = "Data Transfer Object for updating a tag location")
public class UpdateTagLocationDto {

    @Size(max = 255, message = "validation.name.size")
    @Schema(description = "Name of the tag location", example = "Updated Office Entrance")
    private String name;

    @Size(max = 1000, message = "validation.description.size")
    @Schema(description = "Detailed description of the tag location", example = "Updated NFC tag located at the main entrance of the building, next to the reception desk")
    private String description;

    @Size(max = 512, message = "validation.nfcTagUuid.size")
    @Schema(description = "Unique identifier of the NFC tag", example = "6a8008d9-075f-4192-9f89-e2c6428e97c0")
    private String nfcTagUuid;

    @DecimalMin(value = "-90.0", message = "validation.latitude.min")
    @DecimalMax(value = "90.0", message = "validation.latitude.max")
    @Digits(integer = 2, fraction = 8, message = "validation.latitude.precision")
    @Schema(description = "Latitude coordinate of the tag location", example = "51.98765432")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "validation.longitude.min")
    @DecimalMax(value = "180.0", message = "validation.longitude.max")
    @Digits(integer = 3, fraction = 8, message = "validation.longitude.precision")
    @Schema(description = "Longitude coordinate of the tag location", example = "17.87654321")
    private BigDecimal longitude;

    @Size(max = 64, message = "validation.lockedImageFilename.size")
    @Schema(description = "Filename of the image displayed when the tag location is in locked state", example = "760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String lockedImageFilename;

    @Size(max = 64, message = "validation.unlockedImageFilename.size")
    @Schema(description = "Filename of the image displayed when the tag location is in unlocked state", example = "760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String unlockedImageFilename;

    @Valid
    @Schema(description = "List of images that can be unlocked at this tag location")
    private List<CreateTagLocationUnlockImageDto> unlockImages;

}
package pl.osetoctet.tagaccess.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Data Transfer Object for tag access response")
public class TagAccessResponseDto {

    @Schema(description = "Name of the tag location", example = "Main Office Entrance")
    private String name;

    @Schema(description = "Latitude coordinate of the tag location", example = "51.12345678")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate of the tag location", example = "17.12345678")
    private BigDecimal longitude;

    @Schema(description = "", example = "true")
    private boolean isUnlocked;

    @Schema(description = "Details of unlocked tag")
    private TagAccessResponseDetailsDto tagAccessResponseDetails;

}

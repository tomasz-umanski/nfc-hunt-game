package pl.osetoctet.taglocation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

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

}
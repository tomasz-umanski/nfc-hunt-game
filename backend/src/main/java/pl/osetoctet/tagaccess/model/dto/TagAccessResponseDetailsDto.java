package pl.osetoctet.tagaccess.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data Transfer Object for tag access details response")
public class TagAccessResponseDetailsDto {

    @Schema(description = "Detailed description of the tag location", example = "NFC tag located at the main entrance of the building, next to the reception desk")
    private String description;

}

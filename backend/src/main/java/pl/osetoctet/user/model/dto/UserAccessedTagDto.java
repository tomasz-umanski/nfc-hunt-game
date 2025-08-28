package pl.osetoctet.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@Schema(description = "Data Transfer Object for providing user's accessed tag details")
public class UserAccessedTagDto {

    @Schema(description = "Name of the tag location", example = "Main Office Entrance")
    private String name;

    @Schema(description = "Latitude coordinate from which it was unlocked", example = "51.12345678")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate from which it was unlocked", example = "17.12345678")
    private BigDecimal longitude;

    @Schema(description = "Tag unlock date", example = "2024-05-19T12:00:00Z")
    private OffsetDateTime unlockedAt;

}

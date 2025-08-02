package pl.osetoctet.tagaccess.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Data Transfer Object for unlocking a tag location")
public class UnlockTagRequestDto {

    @NotBlank(message = "validation.nfcTagUuid.required")
    @Size(max = 512, message = "validation.nfcTagUuid.size")
    @Schema(description = "Unique identifier of the NFC tag", example = "6a8008d9-075f-4192-9f89-e2c6428e97c0")
    private String nfcTagUuid;

    @NotNull(message = "validation.latitude.required")
    @DecimalMin(value = "-90.0", message = "validation.latitude.min")
    @DecimalMax(value = "90.0", message = "validation.latitude.max")
    @Digits(integer = 2, fraction = 8, message = "validation.latitude.precision")
    @Schema(description = "Latitude coordinate of the tag location", example = "51.12345678")
    private BigDecimal latitude;

    @NotNull(message = "validation.longitude.required")
    @DecimalMin(value = "-180.0", message = "validation.longitude.min")
    @DecimalMax(value = "180.0", message = "validation.longitude.max")
    @Digits(integer = 3, fraction = 8, message = "validation.longitude.precision")
    @Schema(description = "Longitude coordinate of the tag location", example = "17.12345678")
    private BigDecimal longitude;

}

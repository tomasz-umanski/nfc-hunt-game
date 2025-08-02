package pl.osetoctet.upload.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response DTO for file upload containing the file URL")
public class UploadResponseDto {

    @Schema(description = "URL of the uploaded file", example = "/uploads/images/760386df-c8b4-4846-ba95-3b8a54ed340e.jpg")
    private String fileUrl;

}

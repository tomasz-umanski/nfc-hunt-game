package pl.osetoctet.upload;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.osetoctet.common.exception.ErrorResponse;
import pl.osetoctet.upload.model.dto.UploadResponseDto;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
class UploadController {

    private final UploadService uploadService;

    @Operation(operationId = "upload-image", summary = "Upload an Image", tags = {"Uploads"},
            description = "Service used to upload an image to server.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Image uploaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UploadResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported Media Type",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UploadResponseDto> uploadImage(@RequestParam("file") MultipartFile file) {
        UploadResponseDto response = uploadService.uploadFile(file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

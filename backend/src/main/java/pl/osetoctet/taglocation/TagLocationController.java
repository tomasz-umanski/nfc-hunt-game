package pl.osetoctet.taglocation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.osetoctet.common.exception.ErrorResponse;
import pl.osetoctet.taglocation.model.dto.CreateTagLocationDto;
import pl.osetoctet.taglocation.model.dto.TagLocationResponseDto;
import pl.osetoctet.taglocation.model.dto.UpdateTagLocationDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tag-location")
@RequiredArgsConstructor
class TagLocationController {

    private final TagLocationService tagLocationService;

    @Operation(operationId = "create-tag-location", summary = "Create a new tag location", tags = {"Tag location"},
            description = "Service used to create a new tag location.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tag location created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TagLocationResponseDto.class)
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
                            responseCode = "401",
                            description = "Unauthorized",
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
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagLocationResponseDto> createTagLocation(@Valid @RequestBody CreateTagLocationDto createTagLocationDto) {
        TagLocationResponseDto tagLocationResponseDto = tagLocationService.create(createTagLocationDto);
        return new ResponseEntity<>(tagLocationResponseDto, HttpStatus.CREATED);
    }

    @Operation(operationId = "get-all-tag-locations", summary = "Get all tag locations", tags = {"Tag location"},
            description = "Service used to retrieve all tag locations.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tag locations retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TagLocationResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagLocationResponseDto>> getAllTagLocations() {
        List<TagLocationResponseDto> tagLocationResponseDtoList = tagLocationService.getAll();
        return new ResponseEntity<>(tagLocationResponseDtoList, HttpStatus.OK);
    }

    @Operation(operationId = "update-tag-location", summary = "Update a tag location", tags = {"Tag location"},
            description = "Service used to update an existing tag location.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tag location updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TagLocationResponseDto.class)
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
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tag location not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagLocationResponseDto> updateTagLocation(@PathVariable UUID id, @Valid @RequestBody UpdateTagLocationDto updateTagLocationDto) {
        TagLocationResponseDto tagLocationResponseDto = tagLocationService.updateById(id, updateTagLocationDto);
        return new ResponseEntity<>(tagLocationResponseDto, HttpStatus.OK);
    }

    @Operation(operationId = "delete-tag-location", summary = "Delete a tag location", tags = {"Tag location"},
            description = "Service used to delete a tag location by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Tag location deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tag location not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagLocation(@PathVariable UUID id) {
        tagLocationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

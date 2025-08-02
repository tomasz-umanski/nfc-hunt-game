package pl.osetoctet.tagaccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.osetoctet.common.exception.ErrorResponse;
import pl.osetoctet.tagaccess.model.dto.TagAccessResponseDto;
import pl.osetoctet.user.model.entity.User;
import pl.osetoctet.tagaccess.model.dto.UnlockTagRequestDto;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/tag-access")
@RequiredArgsConstructor
class TagAccessController {

    private final TagAccessService tagAccessService;

    @Operation(operationId = "unlock", summary = "Unlock tag for profile", tags = {"Tag access"},
            description = "Service used to unlock tag for a user.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Unlocked tag access"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )),
                    @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @PostMapping(value = "/unlock", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> unlock(@AuthenticationPrincipal User user, @Valid @RequestBody UnlockTagRequestDto unlockTagRequestDto) {
        tagAccessService.unlock(user, unlockTagRequestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "getAllTags", summary = "Get all tags with access-based information", tags = {"Tag access"},
            description = "Retrieves all tags with detailed information for unlocked tags and limited information for locked tags.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved tags",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TagAccessResponseDto.class)
                            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagAccessResponseDto>> getAllTags(@AuthenticationPrincipal User user) {
        List<TagAccessResponseDto> tags;
        if (user == null) {
            tags = tagAccessService.getAllTags();
        } else {
            tags = tagAccessService.getAllTagsForUser(user);
        }
        return ResponseEntity.ok(tags);
    }

}

package pl.osetoctet.user;

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
import pl.osetoctet.user.model.dto.ChangePasswordDto;
import pl.osetoctet.user.model.entity.User;

@Validated
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(operationId = "change-password", summary = "Change User's password", tags = {"User profile"},
            description = "Service used to change password of an existing user.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "User's password changed successfully"),
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
    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal User user, @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userProfileService.changePassword(user, changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "deactivate-account", summary = "Deactivate User's account", tags = {"User profile"},
            description = "Service used to deactivate an existing user account.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "User's account deactivated successfully"),
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
    @DeleteMapping(value = "/deactivate-account")
    public ResponseEntity<Void> deactivateAccount(@AuthenticationPrincipal User user) {
        userProfileService.deactivateAccount(user);
        return ResponseEntity.noContent().build();
    }

}

package pl.osetoctet.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.osetoctet.taglocation.model.dto.TagLocationResponseDto;
import pl.osetoctet.user.model.dto.UserSummaryResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-summary")
@RequiredArgsConstructor
class UserSummaryController {

    private final UserSummaryService userSummaryService;

    @Operation(operationId = "get-all-users", summary = "Get all users", tags = {"User summary"},
            description = "Service used to retrieve all users with info about accessed tags.",
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
                            description = "Users retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TagLocationResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSummaryResponseDto>> getAllUsers() {
        List<UserSummaryResponseDto> userSummaryResponseDtoList = userSummaryService.getAll();
        return new ResponseEntity<>(userSummaryResponseDtoList, HttpStatus.OK);
    }
}

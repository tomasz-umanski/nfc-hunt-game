package pl.osetoctet.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.osetoctet.common.exception.ErrorResponse;
import pl.osetoctet.user.model.dto.AuthenticateUserDto;
import pl.osetoctet.user.model.dto.AuthenticationResponse;
import pl.osetoctet.user.model.dto.RefreshTokenOperationsDto;
import pl.osetoctet.user.model.dto.RegisterUserDto;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(operationId = "register", summary = "Register a User", tags = {"Auth"},
            description = "Service used to register a new user in the system.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    ),
                    @Parameter(
                            name = "Device-Id",
                            description = "Unique identifier for the user's device. Used to associate tokens with specific devices.",
                            in = ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string", example = "device1")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
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
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterUserDto registerUserDto, @RequestHeader("Device-Id") String deviceId) {
        AuthenticationResponse authenticationResponse = authenticationService.register(registerUserDto, deviceId);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }

    @Operation(operationId = "authenticate", summary = "Authenticate a User", tags = {"Auth"},
            description = "Service used to authenticate an existing user in the system.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    ),
                    @Parameter(
                            name = "Device-Id",
                            description = "Unique identifier for the user's device. Used to associate tokens with specific devices.",
                            in = ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string", example = "device1")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "User authenticated", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )),
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
    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticateUserDto authenticateUserDto, @RequestHeader("Device-Id") String deviceId) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticateUserDto, deviceId);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @Operation(operationId = "refresh-token", summary = "Refresh token", tags = {"Auth"},
            description = "Generates a new access and refresh tokens using a valid refresh token. This allows users to stay authenticated without needing to re-login.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    ),
                    @Parameter(
                            name = "Device-Id",
                            description = "Unique identifier for the user's device. Used to associate tokens with specific devices.",
                            in = ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string", example = "device1")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "New access token generated successfully", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthenticationResponse.class)
                    )),
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
    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenOperationsDto refreshTokenOperationsDto, @RequestHeader("Device-Id") String deviceId) {
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshTokenOperationsDto, deviceId);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @Operation(operationId = "logout", summary = "Logout user", tags = {"Auth"},
            description = "Logs out the current user by invalidating their refresh token and clearing the security context.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Preferred language for response messages and error descriptions. Supports standard language tags (e.g., en-US, pl-PL, fr-FR).",
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string", example = "en-US")
                    ),
                    @Parameter(
                            name = "Device-Id",
                            description = "Unique identifier for the user's device. Used to associate tokens with specific devices.",
                            in = ParameterIn.HEADER,
                            required = true,
                            schema = @Schema(type = "string", example = "device1")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "User logout successfully"),
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
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenOperationsDto refreshTokenOperationsDto, @RequestHeader("Device-Id") String deviceId) {
        authenticationService.logout(refreshTokenOperationsDto, deviceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "logout-from-all-devices", summary = "Logout user from all devices", tags = {"Auth"},
            description = "Logs out the current user from every device by invalidating their refresh tokens and clearing the security context.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User logout from all devices successfully"),
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
    @PostMapping("/logout-from-all-devices")
    public ResponseEntity<Void> logoutFromAllDevices(@Valid @RequestBody RefreshTokenOperationsDto refreshTokenOperationsDto) {
        authenticationService.logoutFromAllDevices(refreshTokenOperationsDto);
        return ResponseEntity.noContent().build();
    }

}

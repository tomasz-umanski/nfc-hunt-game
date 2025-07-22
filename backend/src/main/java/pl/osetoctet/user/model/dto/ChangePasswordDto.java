package pl.osetoctet.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.osetoctet.user.validator.ChangedPasswordMatches;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Data Transfer Object for changing password of an existing user")
@ChangedPasswordMatches()
public class ChangePasswordDto {

    @NotBlank(message = "validation.currentPassword.required")
    @Size(min = 8, max = 100, message = "validation.currentPassword.size")
    @Schema(description = "User's current password", example = "strongpassword123")
    private String currentPassword;

    @NotBlank(message = "validation.newPassword.required")
    @Size(min = 8, max = 100, message = "validation.newPassword.size")
    @Schema(description = "User's new password", example = "strongpassword123")
    private String newPassword;

    @NotBlank(message = "validation.confirmNewPassword.required")
    @Size(max = 100, message = "validation.confirmNewPassword.size")
    @Schema(description = "User's new password confirmation", example = "strongpassword123")
    private String confirmNewPassword;

    @NotBlank(message = "validation.token.required")
    @Size(max = 512, message = "validation.token.size")
    @Schema(description = "Refresh token used for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

}

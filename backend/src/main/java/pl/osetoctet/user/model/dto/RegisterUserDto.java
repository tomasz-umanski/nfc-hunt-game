package pl.osetoctet.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import pl.osetoctet.user.validator.PasswordMatches;

@Data
@Builder
@Schema(description = "Data Transfer Object for registering a new user")
@PasswordMatches()
public class RegisterUserDto {

    @Email(message = "validation.email.format")
    @NotBlank(message = "validation.email.required")
    @Size(max = 100, message = "validation.email.size")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "validation.password.required")
    @Size(min = 8, max = 100, message = "validation.password.size")
    @Schema(description = "User's password", example = "strongpassword123")
    private String password;

    @NotBlank(message = "validation.confirmPassword.required")
    @Size(max = 100, message = "validation.confirmPassword.size")
    @Schema(description = "User's password confirmation", example = "strongpassword123")
    private String confirmPassword;

    @NotNull(message = "validation.terms.gameRules.required")
    @AssertTrue(message = "validation.terms.gameRules.required")
    @Schema(description = "Acceptance of game rules and terms", example = "true")
    private Boolean acceptGameRules;

    @NotNull(message = "validation.terms.privacyPolicy.required")
    @AssertTrue(message = "validation.terms.privacyPolicy.required")
    @Schema(description = "Acceptance of privacy policy and data processing terms", example = "true")
    private Boolean acceptPrivacyPolicy;

    @NotNull(message = "validation.terms.ageConfirmation.required")
    @AssertTrue(message = "validation.terms.ageConfirmation.required")
    @Schema(description = "Confirmation that user is at least 16 years old", example = "true")
    private Boolean confirmAge;

    public void trimFields() {
        this.email = (email != null) ? email.trim() : null;
        this.password = (password != null) ? password.trim() : null;
        this.confirmPassword = (confirmPassword != null) ? confirmPassword.trim() : null;
    }

}

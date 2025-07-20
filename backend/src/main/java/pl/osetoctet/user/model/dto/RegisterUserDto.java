package pl.osetoctet.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    public void trimFields() {
        this.email = (email != null) ? email.trim() : null;
        this.password = (password != null) ? password.trim() : null;
        this.confirmPassword = (confirmPassword != null) ? confirmPassword.trim() : null;
    }

}

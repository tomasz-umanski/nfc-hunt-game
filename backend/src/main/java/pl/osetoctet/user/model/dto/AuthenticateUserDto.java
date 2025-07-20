package pl.osetoctet.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data Transfer Object for authenticating an existing user")
public class AuthenticateUserDto {

    @Email(message = "validation.email.format")
    @NotBlank(message = "validation.email.required")
    @Size(max = 100, message = "validation.email.size")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "validation.password.required")
    @Size(min = 8, max = 100, message = "validation.password.size")
    @Schema(description = "User's password", example = "strongpassword123")
    private String password;

}

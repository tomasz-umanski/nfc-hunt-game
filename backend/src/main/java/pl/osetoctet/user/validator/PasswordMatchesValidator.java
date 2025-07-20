package pl.osetoctet.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.osetoctet.user.model.dto.RegisterUserDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserDto> {

    @Override
    public boolean isValid(RegisterUserDto dto, ConstraintValidatorContext context) {
        return dto.getPassword() != null && dto.getPassword().equals(dto.getConfirmPassword());
    }

}

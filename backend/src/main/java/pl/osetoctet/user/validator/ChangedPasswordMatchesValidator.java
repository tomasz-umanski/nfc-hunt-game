package pl.osetoctet.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.osetoctet.user.model.dto.ChangePasswordDto;

public class ChangedPasswordMatchesValidator implements ConstraintValidator<ChangedPasswordMatches, ChangePasswordDto> {

    @Override
    public boolean isValid(ChangePasswordDto dto, ConstraintValidatorContext context) {
        if (dto.getNewPassword() == null || dto.getConfirmNewPassword() == null) {
            return false;
        }
        return dto.getNewPassword().equals(dto.getConfirmNewPassword());
    }

}

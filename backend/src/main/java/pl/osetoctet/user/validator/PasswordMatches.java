package pl.osetoctet.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
    String message() default "validation.password.matches";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

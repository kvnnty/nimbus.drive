package drive.nimbus.api.v1.validation.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import drive.nimbus.api.v1.validation.validators.NationalIdConstraintValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NationalIdConstraintValidator.class)
@Target({ FIELD, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNationalId {
  String message() default "Invalid national id";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
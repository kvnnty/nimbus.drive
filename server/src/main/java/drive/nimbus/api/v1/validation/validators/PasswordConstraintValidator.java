package drive.nimbus.api.v1.validation.validators;

import java.util.Arrays;

import org.passay.*;

import drive.nimbus.api.v1.validation.annotations.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    PasswordValidator passwordValidator = new PasswordValidator(
        Arrays.asList(
            new LengthRule(8, 20),
            new UppercaseCharacterRule(1),
            new SpecialCharacterRule(1),
            new DigitCharacterRule(1),
            new WhitespaceRule()));

    RuleResult result = passwordValidator.validate(new PasswordData(password));

    if (result.isValid()) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    // Add individual error messages for each rule violation
    passwordValidator.getMessages(result).forEach(error -> {
      context.buildConstraintViolationWithTemplate(error)
          .addConstraintViolation();
    });
    return false;
  }

}

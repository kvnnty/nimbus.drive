package drive.nimbus.api.v1.validation.validators;

import java.util.regex.Pattern;

import drive.nimbus.api.v1.validation.annotations.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberConstraintValidator implements ConstraintValidator<ValidPhoneNumber, String> {

  private static final String PHONE_NUMBER_PATTERN = "^(\\+\\d{1,3})?\\d{7,15}$";

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    if (phoneNumber == null) {
      return false;
    }
    return Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber);
  }

}

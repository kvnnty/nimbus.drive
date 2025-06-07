package drive.nimbus.api.v1.utils.helpers;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import drive.nimbus.api.v1.repositories.otp.OtpRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OtpUtil {

  private final OtpRepository otpRepository;
  private final SecureRandom secureRandom = new SecureRandom();

  public String generateOtp() {
    String code;
    do {
      code = String.format("%06d", secureRandom.nextInt(1_000_000));
    } while (otpRepository.existsByCode(code));
    return code;
  }
}

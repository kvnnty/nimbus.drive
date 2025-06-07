package drive.nimbus.api.v1.services.otp;

import drive.nimbus.api.v1.entities.user.Otp;
import drive.nimbus.api.v1.enums.otp.OtpPurpose;

public interface OtpService {
  Otp createOtp(String email, OtpPurpose otpPurpose);

  void validateOtp(String email, String code, OtpPurpose purpose);
}

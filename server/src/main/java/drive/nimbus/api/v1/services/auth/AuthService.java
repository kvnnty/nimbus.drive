package drive.nimbus.api.v1.services.auth;

import drive.nimbus.api.v1.dtos.requests.auth.LoginRequestDTO;
import drive.nimbus.api.v1.dtos.responses.auth.AuthResponseDTO;

public interface AuthService {
  AuthResponseDTO login(LoginRequestDTO requestDTO);

  void verifyAccount(String email, String code);

  void resendAccountVerificationCode(String email);

  void forgotPassword(String email);

  void resetPassword(String email, String code, String newPassword);

  void resendPasswordResetCode(String email);
}

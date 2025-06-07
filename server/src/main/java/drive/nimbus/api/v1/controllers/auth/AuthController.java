package drive.nimbus.api.v1.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import drive.nimbus.api.v1.dtos.requests.auth.EmailRequest;
import drive.nimbus.api.v1.dtos.requests.auth.LoginRequestDTO;
import drive.nimbus.api.v1.dtos.requests.auth.AccountVerificationRequest;
import drive.nimbus.api.v1.dtos.requests.auth.ResetPasswordRequest;
import drive.nimbus.api.v1.dtos.responses.auth.AuthResponseDTO;
import drive.nimbus.api.v1.payload.ApiResponse;
import drive.nimbus.api.v1.services.auth.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication Resource", description = "APIs for user authentication operations")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
    AuthResponseDTO authResponse = authService.login(requestDTO);
    return ApiResponse.success("Logged in successfully", HttpStatus.OK, authResponse);
  }

  @PostMapping("/verify-account")
  public ResponseEntity<ApiResponse<Object>> verifyAccount(@RequestBody @Valid AccountVerificationRequest request) {
    authService.verifyAccount(request.getEmail(), request.getCode());
    return ApiResponse.success("Account successfully verified", HttpStatus.OK, null);
  }

  @PostMapping("/resend-account-verification-code")
  public ResponseEntity<ApiResponse<Object>> resendAccountVerificationCode(@RequestBody @Valid EmailRequest request) {
    authService.resendAccountVerificationCode(request.getEmail());
    return ApiResponse.success("Verification code resent to email", HttpStatus.OK, null);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<ApiResponse<Object>> forgotPassword(@RequestBody @Valid EmailRequest request) {
    authService.forgotPassword(request.getEmail());
    return ApiResponse.success("Reset password instructions sent to email", HttpStatus.OK, null);
  }

  @PostMapping("/resend-password-reset-code")
  public ResponseEntity<ApiResponse<Object>> resendPasswordResetCode(@RequestBody @Valid EmailRequest request) {
    authService.resendPasswordResetCode(request.getEmail());
    return ApiResponse.success("Password reset code resent to email", HttpStatus.OK, null);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    authService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
    return ApiResponse.success("Password has been reset successfully. Use your new password to login", HttpStatus.OK,
        null);
  }
}

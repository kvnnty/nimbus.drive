package drive.nimbus.api.v1.dtos.requests.auth;

import drive.nimbus.api.v1.enums.otp.OtpPurpose;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerificationRequest {
  @NotBlank(message = "Email address is required")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Please enter your verification Code")
  private String code;

  @NotNull(message = "Purpose cannot be blank")
  private OtpPurpose purpose;
}
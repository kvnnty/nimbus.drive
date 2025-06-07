package drive.nimbus.api.v1.dtos.requests.auth;

import drive.nimbus.api.v1.validation.annotations.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
  @Email(message = "Please enter a valid email address")
  @NotBlank(message = "Email address is required")
  private String email;

  @NotBlank(message = "Please enter your new password")
  @ValidPassword
  private String newPassword;

  @NotBlank(message = "Please enter your verification Code")
  private String code;
}
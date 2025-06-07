package drive.nimbus.api.v1.dtos.requests.user;

import drive.nimbus.api.v1.validation.annotations.ValidNationalId;
import drive.nimbus.api.v1.validation.annotations.ValidPassword;
import drive.nimbus.api.v1.validation.annotations.ValidPhoneNumber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

  @NotBlank(message = "Full name is required")
  private String names;

  @NotBlank(message = "Email is required")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Password is required")
  @ValidPassword
  private String password;

  @NotBlank(message = "Phone number is required")
  @ValidPhoneNumber
  private String phoneNumber;

  @NotBlank(message = "National ID is required")
  @ValidNationalId
  private String nationalId;
}

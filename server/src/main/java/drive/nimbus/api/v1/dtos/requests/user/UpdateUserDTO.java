package drive.nimbus.api.v1.dtos.requests.user;

import drive.nimbus.api.v1.validation.annotations.ValidNationalId;
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
public class UpdateUserDTO {

  @NotBlank(message = "Names cannot be blank")
  private String names;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Phone number cannot be blank")
  @ValidPhoneNumber
  private String phoneNumber;

  @NotBlank(message = "National ID cannot be blank")
  @ValidNationalId
  private String nationalId;

}

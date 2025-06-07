package drive.nimbus.api.v1.dtos.responses.users;

import java.util.UUID;

import drive.nimbus.api.v1.enums.user.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
  private UUID id;
  private String names;
  private String email;
  private String phoneNumber;
  private String nationalId;
  private ERole role;
}

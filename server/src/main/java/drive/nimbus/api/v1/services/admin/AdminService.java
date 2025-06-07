package drive.nimbus.api.v1.services.admin;

import java.util.List;
import java.util.UUID;

import drive.nimbus.api.v1.dtos.requests.user.CreateUserDTO;
import drive.nimbus.api.v1.dtos.requests.user.UpdateUserDTO;
import drive.nimbus.api.v1.dtos.responses.users.UserResponseDTO;

public interface AdminService {
  void createAdmin(CreateUserDTO requestDto);

  void deleteAdmin(UUID adminId);

  List<UserResponseDTO> getAllAdmins();

  void updateAdmin(UUID adminId, UpdateUserDTO updateUserDTO);
}

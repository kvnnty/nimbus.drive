package drive.nimbus.api.v1.services.users;

import drive.nimbus.api.v1.dtos.requests.user.ChangePasswordDTO;
import drive.nimbus.api.v1.dtos.requests.user.CreateUserDTO;
import drive.nimbus.api.v1.dtos.requests.user.UpdateUserDTO;
import drive.nimbus.api.v1.dtos.responses.users.UserResponseDTO;
import drive.nimbus.api.v1.entities.user.User;

public interface UserService {
  void registerUser(CreateUserDTO requestDTO);

  UserResponseDTO getMyprofile();

  User getCurrentUser();

  void updateUserProfile(UpdateUserDTO dto);

  void changePassword(ChangePasswordDTO dto);

  void deleteCurrentUser();

}

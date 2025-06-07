package drive.nimbus.api.v1.controllers.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import drive.nimbus.api.v1.dtos.requests.user.ChangePasswordDTO;
import drive.nimbus.api.v1.dtos.requests.user.CreateUserDTO;
import drive.nimbus.api.v1.dtos.requests.user.UpdateUserDTO;
import drive.nimbus.api.v1.dtos.responses.users.UserResponseDTO;
import drive.nimbus.api.v1.payload.ApiResponse;
import drive.nimbus.api.v1.services.users.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "APIs for managing user account related operations")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Object>> registerCustomer(@Valid @RequestBody CreateUserDTO requestDTO) {
    userService.registerUser(requestDTO);
    return ApiResponse.success("Account created successfully, please check your email to verify your account",
        HttpStatus.CREATED, null);
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getMyProfile() {
    return ApiResponse.success("User profile retrieved", HttpStatus.OK, userService.getMyprofile());
  }

  @PutMapping("/update-profile")
  public ResponseEntity<ApiResponse<Object>> updateProfile(@Valid @RequestBody UpdateUserDTO updateDTO) {
    userService.updateUserProfile(updateDTO);
    return ApiResponse.success("Profile updated successfully", HttpStatus.OK, null);
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
    userService.changePassword(dto);
    return ApiResponse.success("Password changed successfully", HttpStatus.OK, null);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponse<Object>> deleteAccount() {
    userService.deleteCurrentUser();
    return ApiResponse.success("Account deleted successfully", HttpStatus.OK, null);
  }
}

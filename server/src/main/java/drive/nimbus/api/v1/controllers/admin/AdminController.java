package drive.nimbus.api.v1.controllers.admin;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import drive.nimbus.api.v1.dtos.requests.user.CreateUserDTO;
import drive.nimbus.api.v1.dtos.requests.user.UpdateUserDTO;
import drive.nimbus.api.v1.payload.ApiResponse;
import drive.nimbus.api.v1.services.admin.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Super Admin Resource", description = "APIs for managing admin accounts and operations")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<Object>> createAdmin(@Valid @RequestBody CreateUserDTO createAdminDTO) {
    adminService.createAdmin(createAdminDTO);
    return ApiResponse.success("Admin created successfully", HttpStatus.CREATED, null);
  }

  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @DeleteMapping("/{adminId}")
  public ResponseEntity<ApiResponse<Object>> deleteAdmin(@PathVariable UUID adminId) {
    adminService.deleteAdmin(adminId);
    return ApiResponse.success("Admin deleted successfully", HttpStatus.OK, null);
  }

  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @GetMapping("/all-admins")
  public ResponseEntity<ApiResponse<Object>> getAllAdmins() {
    var admins = adminService.getAllAdmins();
    return ApiResponse.success("All admins fetched", HttpStatus.OK, admins);
  }

  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @PutMapping("/{adminId}")
  public ResponseEntity<ApiResponse<Object>> updateAdmin(@PathVariable UUID adminId,
      @Valid @RequestBody UpdateUserDTO updateUserDTO) {
    adminService.updateAdmin(adminId, updateUserDTO);
    return ApiResponse.success("Admin updated successfully", HttpStatus.OK, null);
  }
}

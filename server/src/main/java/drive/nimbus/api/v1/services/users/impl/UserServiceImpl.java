package drive.nimbus.api.v1.services.users.impl;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import drive.nimbus.api.v1.dtos.requests.user.ChangePasswordDTO;
import drive.nimbus.api.v1.dtos.requests.user.CreateUserDTO;
import drive.nimbus.api.v1.dtos.requests.user.UpdateUserDTO;
import drive.nimbus.api.v1.dtos.responses.users.UserResponseDTO;
import drive.nimbus.api.v1.entities.user.Otp;
import drive.nimbus.api.v1.entities.user.Role;
import drive.nimbus.api.v1.entities.user.User;
import drive.nimbus.api.v1.enums.otp.OtpPurpose;
import drive.nimbus.api.v1.enums.user.ERole;
import drive.nimbus.api.v1.exceptions.BadRequestException;
import drive.nimbus.api.v1.exceptions.DuplicateResourceException;
import drive.nimbus.api.v1.repositories.roles.RoleRepository;
import drive.nimbus.api.v1.repositories.users.UserRepository;
import drive.nimbus.api.v1.security.user.UserPrincipal;
import drive.nimbus.api.v1.services.mail.MailService;
import drive.nimbus.api.v1.services.otp.OtpService;
import drive.nimbus.api.v1.services.users.UserService;
import drive.nimbus.api.v1.utils.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final OtpService otpService;
  private final MailService mailService;

  @Override
  public void registerUser(CreateUserDTO requestDTO) {
    if (userRepository.existsByEmail(requestDTO.getEmail())) {
      throw new DuplicateResourceException("Email is already in use.");
    }
    if (userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
      throw new DuplicateResourceException("Phone number is already in use.");
    }
    if (userRepository.existsByNationalId(requestDTO.getNationalId())) {
      throw new DuplicateResourceException("National ID is already registered.");
    }

    Role customerRole = roleRepository.findByType(ERole.ROLE_USER)
        .orElseThrow(() -> new IllegalStateException("Role not found"));

    User newCustomer = User.builder()
        .names(requestDTO.getNames())
        .email(requestDTO.getEmail())
        .password(passwordEncoder.encode(requestDTO.getPassword()))
        .phoneNumber(requestDTO.getPhoneNumber())
        .nationalId(requestDTO.getNationalId())
        .isVerified(false)
        .role(customerRole)
        .build();

    userRepository.save(newCustomer);
    Otp otp = otpService.createOtp(newCustomer.getEmail(), OtpPurpose.VERIFICATION);

    String message = String.format(
        "Hello %s,\n\nUse the following code to verify your account: %s\nThis code will expire in 10 minutes.\n\nThanks,\nKevin",
        newCustomer.getNames(), otp.getCode());
    mailService.sendMail(newCustomer.getEmail(), "Account Verification Code", message);
  }

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
    }

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    User user = userRepository.findByEmail(userPrincipal.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return user;
  }

  @Override
  @Transactional
  public void updateUserProfile(UpdateUserDTO dto) {
    User user = getCurrentUser();

    if (dto.getPhoneNumber().equals(user.getPhoneNumber())) {
      throw new BadRequestException("The new phone number cannot be the same as your current one.");
    }
    if (dto.getEmail().equals(user.getEmail())) {
      throw new BadRequestException("The new email cannot be the same as your current one.");
    }
    if (dto.getNationalId().equals(user.getNationalId())) {
      throw new BadRequestException("The new national ID cannot be the same as your current one.");
    }

    if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
      throw new DuplicateResourceException("Phone number is already in use.");
    }
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new DuplicateResourceException("Email is already in use.");
    }
    if (userRepository.existsByNationalId(dto.getNationalId())) {
      throw new DuplicateResourceException("National ID is already in use.");
    }

    user.setNames(dto.getNames());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setEmail(dto.getEmail());
    user.setNationalId(dto.getNationalId());

    userRepository.save(user);
  }

  @Override
  public void changePassword(ChangePasswordDTO dto) {
    User user = getCurrentUser();

    if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
      throw new BadCredentialsException("Current password is incorrect.");
    }

    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void deleteCurrentUser() {
    User user = getCurrentUser();
    userRepository.delete(user);
  }

  @Override
  public UserResponseDTO getMyprofile() {
    User user = this.getCurrentUser();
    return UserMapper.toDto(user);
  }

}

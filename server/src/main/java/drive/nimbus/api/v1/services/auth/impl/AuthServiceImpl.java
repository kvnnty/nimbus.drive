package drive.nimbus.api.v1.services.auth.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import drive.nimbus.api.v1.dtos.requests.auth.LoginRequestDTO;
import drive.nimbus.api.v1.dtos.responses.auth.AuthResponseDTO;
import drive.nimbus.api.v1.entities.user.Otp;
import drive.nimbus.api.v1.entities.user.User;
import drive.nimbus.api.v1.enums.otp.OtpPurpose;
import drive.nimbus.api.v1.exceptions.BadRequestException;
import drive.nimbus.api.v1.repositories.otp.OtpRepository;
import drive.nimbus.api.v1.repositories.users.UserRepository;
import drive.nimbus.api.v1.security.jwt.JwtTokenService;
import drive.nimbus.api.v1.services.auth.AuthService;
import drive.nimbus.api.v1.services.mail.MailService;
import drive.nimbus.api.v1.services.otp.OtpService;
import drive.nimbus.api.v1.services.users.UserService;
import drive.nimbus.api.v1.utils.mappers.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;
  private final MailService mailService;
  private final UserRepository userRepository;
  private final OtpRepository otpRepository;
  private final OtpService otpService;

  @Override
  public AuthResponseDTO login(LoginRequestDTO requestDTO) {
    Authentication authentication = authentication(requestDTO);

    if (!authentication.isAuthenticated()) {
      throw new BadRequestException("Couldn't authenticate request");
    }

    User user = userService.getCurrentUser();

    if (!user.isVerified()) {
      throw new BadRequestException("Account not verified. Please verify your email to activate your account.");
    }

    return AuthResponseDTO.builder()
        .user(UserMapper.toDto(user))
        .token(generateJwtToken(authentication))
        .build();

  }

  public void forgotPassword(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Otp otp = otpService.createOtp(user.getEmail(), OtpPurpose.RESET_PASSWORD);

    String message = String.format(
        "Hello %s,\n\nUse the following code to complete your password reset request: %s\nThis code will expire in 10 minutes.\n\nThanks,\nstadrive.nimbus.api Team",
        user.getNames(), otp.getCode());
    mailService.sendMail(email, "Password Reset OTP", message);
  }

  @Override
  public void resetPassword(String email, String code, String newPassword) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Otp otp = otpRepository.findByUserAndPurposeAndCodeAndIsUsedIsFalse(user, OtpPurpose.RESET_PASSWORD, code)
        .orElseThrow(() -> new BadRequestException("Invalid or expired OTP"));

    if (otp.getExpiresAt().isBefore(LocalDateTime.now()))
      throw new BadRequestException("OTP has expired, request a new one!");

    otp.setUsed(true);
    otpRepository.save(otp);

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  @Override
  public void verifyAccount(String email, String code) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Otp otp = otpRepository.findByUserAndPurposeAndCodeAndIsUsedIsFalse(user, OtpPurpose.VERIFICATION, code)
        .orElseThrow(() -> new BadRequestException("Invalid or expired verification code"));

    if (otp.getExpiresAt().isBefore(LocalDateTime.now()))
      throw new BadRequestException("Verification code has expired");

    otp.setUsed(true);
    otpRepository.save(otp);

    user.setVerified(true);
    userRepository.save(user);
  }

  @Override
  public void resendAccountVerificationCode(String email) {
    resendOtp(email, OtpPurpose.VERIFICATION, "Account Verification Code",
        (user, otp) -> String.format(
            "Hello %s,\n\nUse the following code to verify your account: %s\nThis code will expire in 10 minutes.\n\nThanks,\nstadrive.nimbus.api Team",
            user.getNames(), otp.getCode()));
  }

  @Override
  public void resendPasswordResetCode(String email) {
    resendOtp(email, OtpPurpose.RESET_PASSWORD, "Password Reset Code",
        (user, otp) -> String.format(
            "Hello %s,\n\nUse the following code to complete your password reset request: %s\nThis code will expire in 10 minutes.\n\nThanks,\nstadrive.nimbus.api Team",
            user.getNames(), otp.getCode()));
  }

  private void resendOtp(String email, OtpPurpose purpose, String subject,
      BiFunction<User, Otp, String> messageBuilder) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (purpose == OtpPurpose.VERIFICATION && user.isVerified()) {
      throw new IllegalStateException("Account is already verified");
    }

    // Mark previous OTPs as used
    List<Otp> previousOtps = otpRepository.findAllByUserAndPurposeAndIsUsedFalse(user, purpose);
    previousOtps.forEach(otp -> otp.setUsed(true));
    otpRepository.saveAll(previousOtps);

    // Create and send new OTP
    Otp otp = otpService.createOtp(email, purpose);
    String message = messageBuilder.apply(user, otp);
    mailService.sendMail(email, subject, message);
  }

  private Authentication authentication(LoginRequestDTO requestDTO) {
    try {
      UsernamePasswordAuthenticationToken authenticationRequest = new UsernamePasswordAuthenticationToken(
          requestDTO.getEmail(), requestDTO.getPassword());
      Authentication authentication = authenticationManager.authenticate(authenticationRequest);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return authentication;

    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Incorrect email or password");
    }
  }

  private String generateJwtToken(Authentication authentication) {
    return jwtTokenService.generateToken(authentication);
  }

}

package drive.nimbus.api.v1.repositories.otp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import drive.nimbus.api.v1.entities.user.Otp;
import drive.nimbus.api.v1.entities.user.User;
import drive.nimbus.api.v1.enums.otp.OtpPurpose;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
  boolean existsByCode(String code);

  Optional<Otp> findTopByUserAndPurposeOrderByCreatedAtDesc(User user, OtpPurpose purpose);

  Optional<Otp> findByUserAndPurposeAndCodeAndIsUsedIsFalse(User user, OtpPurpose purpose, String code);

  List<Otp> findAllByUserAndPurposeAndIsUsedFalse(User user, OtpPurpose purpose);
}

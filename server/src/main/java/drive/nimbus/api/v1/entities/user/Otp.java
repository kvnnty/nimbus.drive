package drive.nimbus.api.v1.entities.user;

import java.time.LocalDateTime;
import java.util.UUID;

import drive.nimbus.api.v1.enums.otp.OtpPurpose;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "otps")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Otp {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 6)
  private String code;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OtpPurpose purpose;

  @Column(nullable = false)
  private boolean isUsed;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(updatable = false)
  private LocalDateTime createdAt;

}

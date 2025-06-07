package drive.nimbus.api.v1.repositories.roles;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import drive.nimbus.api.v1.entities.user.Role;
import drive.nimbus.api.v1.enums.user.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByType(ERole type);

  boolean existsByType(ERole type);
}
package rocks.ifa.spring.domain.clientProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfileEntity, Long> {
    Optional<ClientProfileEntity> findByAgentFirebaseUid(String agentFirebaseUid);
    boolean existsByAgentFirebaseUid(String agentFirebaseUid);
    void deleteByAgentFirebaseUid(String agentFirebaseUid);
}

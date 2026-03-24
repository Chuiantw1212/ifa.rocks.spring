package rocks.ifa.spring.domain.clientRetirement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRetirementRepository extends JpaRepository<ClientRetirementEntity, Long> {
    Optional<ClientRetirementEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}

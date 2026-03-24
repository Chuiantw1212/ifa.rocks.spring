package rocks.ifa.spring.domain.clientRetirement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRetirementRepository extends JpaRepository<ClientRetirementEntity, UUID> {
    Optional<ClientRetirementEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}

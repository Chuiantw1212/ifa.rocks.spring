package rocks.ifa.spring.domain.clientLaborPension;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientLaborPensionRepository extends JpaRepository<ClientLaborPensionEntity, Long> {
    Optional<ClientLaborPensionEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}

package rocks.ifa.spring.domain.clientCareer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientCareerRepository extends JpaRepository<ClientCareerEntity, UUID> {
    Optional<ClientCareerEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}

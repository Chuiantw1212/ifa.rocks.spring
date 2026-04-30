package rocks.ifa.spring.domain.clientCareer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientCareerRepository extends JpaRepository<ClientCareerEntity, UUID> {
    
    // We can remove findByClientId because JpaRepository already provides findById.
    // Optional<ClientCareerEntity> findById(UUID id);

    // This might be useful if an agent needs to find a career record directly.
    Optional<ClientCareerEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}

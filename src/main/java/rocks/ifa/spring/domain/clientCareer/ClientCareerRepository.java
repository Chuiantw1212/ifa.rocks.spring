package rocks.ifa.spring.domain.clientCareer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientCareerRepository extends JpaRepository<ClientCareerEntity, UUID> {
    Optional<ClientCareerEntity> findByAgentFirebaseUid(String agentFirebaseUid);

    /**
     * Finds a client's career details by their unique client ID (from client_profiles).
     * @param clientId The client's unique identifier.
     * @return An Optional containing the career entity if found.
     */
    Optional<ClientCareerEntity> findByClientId(UUID clientId);
}

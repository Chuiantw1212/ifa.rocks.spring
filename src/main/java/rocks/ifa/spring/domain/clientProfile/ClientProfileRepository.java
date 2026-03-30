package rocks.ifa.spring.domain.clientProfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ClientProfileRepository extends JpaRepository<ClientProfileEntity, UUID> {
    Optional<ClientProfileEntity> findByAgentFirebaseUid(String agentFirebaseUid);
    boolean existsByAgentFirebaseUid(String agentFirebaseUid);
    void deleteByAgentFirebaseUid(String agentFirebaseUid);
    Page<ClientProfileEntity> findAllByAgentFirebaseUid(String agentFirebaseUid, Pageable pageable);

    Optional<ClientProfileEntity> findByEmail(String email);

    Optional<ClientProfileEntity> findByClientFirebaseUid(String clientFirebaseUid);

    /**
     * Finds all profiles where the user is either the agent or the client themselves.
     * @param agentFirebaseUid The agent's Firebase UID.
     * @param clientFirebaseUid The user's own Firebase UID (can be the same as agentFirebaseUid).
     * @param pageable The pagination information.
     * @return A page of client profiles.
     */
    Page<ClientProfileEntity> findAllByAgentFirebaseUidOrClientFirebaseUid(String agentFirebaseUid, String clientFirebaseUid, Pageable pageable);
}

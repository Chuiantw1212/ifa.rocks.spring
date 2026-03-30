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

    /**
     * Finds a client profile by their own Firebase UID.
     * Used for fetching a client's own data after they have logged in.
     * @param clientFirebaseUid The client's own Firebase UID.
     * @return An Optional containing the client profile if found.
     */
    Optional<ClientProfileEntity> findByClientFirebaseUid(String clientFirebaseUid);
}

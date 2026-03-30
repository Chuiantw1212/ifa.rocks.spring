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

    /**
     * Finds a client profile by their email address.
     * Used for client login and account binding.
     * @param email The client's email address.
     * @return An Optional containing the client profile if found.
     */
    Optional<ClientProfileEntity> findByEmail(String email);
}

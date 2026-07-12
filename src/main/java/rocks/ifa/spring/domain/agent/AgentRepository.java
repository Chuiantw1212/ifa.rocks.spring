package rocks.ifa.spring.domain.agent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {
    Optional<Agent> findByFirebaseUid(String firebaseUid);
    Optional<Agent> findByLineUserId(String lineUserId);
    Optional<Agent> findByEmail(String email);
}

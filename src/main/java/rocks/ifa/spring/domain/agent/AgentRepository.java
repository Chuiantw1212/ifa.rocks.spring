package rocks.ifa.spring.domain.agent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, UUID> {

    Optional<AgentEntity> findByFirebaseUid(String firebaseUid);

    Optional<AgentEntity> findByLineUserId(String lineUserId);

    Optional<AgentEntity> findByEmail(String email);
}

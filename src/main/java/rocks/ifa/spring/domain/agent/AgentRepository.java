package rocks.ifa.spring.domain.agent;

import java.util.Optional;
import java.util.UUID;

public interface AgentRepository {

    Optional<Agent> findByFirebaseUid(String firebaseUid);

    Optional<Agent> findByLineUserId(String lineUserId);

    Optional<Agent> findByEmail(String email);

    Agent save(Agent agent);
}

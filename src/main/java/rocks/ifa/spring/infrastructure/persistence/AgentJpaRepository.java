package rocks.ifa.spring.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.ifa.spring.infrastructure.persistence.po.AgentPO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentJpaRepository extends JpaRepository<AgentPO, UUID> {
    Optional<AgentPO> findByFirebaseUid(String firebaseUid);
    Optional<AgentPO> findByLineUserId(String lineUserId);
    Optional<AgentPO> findByEmail(String email);
}

package rocks.ifa.spring.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import rocks.ifa.spring.domain.agent.Agent;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.infrastructure.persistence.po.AgentPO;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AgentRepositoryImpl implements AgentRepository {

    private final AgentJpaRepository agentJpaRepository;

    @Override
    public Optional<Agent> findByFirebaseUid(String firebaseUid) {
        return agentJpaRepository.findByFirebaseUid(firebaseUid).map(this::toDomain);
    }

    @Override
    public Optional<Agent> findByLineUserId(String lineUserId) {
        return agentJpaRepository.findByLineUserId(lineUserId).map(this::toDomain);
    }

    @Override
    public Optional<Agent> findByEmail(String email) {
        return agentJpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Agent save(Agent agent) {
        AgentPO agentPO = toPO(agent);
        AgentPO savedPO = agentJpaRepository.save(agentPO);
        return toDomain(savedPO);
    }

    // --- Private Mappers ---

    private Agent toDomain(AgentPO po) {
        if (po == null) {
            return null;
        }
        // Use the factory method from the domain object
        return Agent.fromPersistence(
                po.getId(),
                po.getFirebaseUid(),
                po.getLineUserId(),
                po.getEmail(),
                po.getName(),
                po.getPictureUrl()
        );
    }

    private AgentPO toPO(Agent domain) {
        if (domain == null) {
            return null;
        }
        AgentPO po = new AgentPO();
        po.setId(domain.getId());
        po.setFirebaseUid(domain.getFirebaseUid());
        po.setLineUserId(domain.getLineUserId());
        po.setEmail(domain.getEmail());
        po.setName(domain.getName());
        po.setPictureUrl(domain.getPictureUrl());
        return po;
    }
}

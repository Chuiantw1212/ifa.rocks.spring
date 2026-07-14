package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.common.config.SecurityUtils;
import rocks.ifa.spring.domain.agent.dtos.AgentRecord;
import rocks.ifa.spring.domain.agent.dtos.UpdateAgentReq;
import rocks.ifa.spring.domain.auth.dtos.LineTokenPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final FirebaseAuth firebaseAuth;
    private final AgentRepository agentRepository;
    private final AgentMapper agentMapper;

    @Override
    @Transactional
    public AgentRecord getAgentByFirebaseUid(String firebaseUid) {
        Agent agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> {
                    try {
                        UserRecord userRecord = firebaseAuth.getUser(firebaseUid);
                        Agent newAgent = new Agent();
                        newAgent.setFirebaseUid(userRecord.getUid());
                        newAgent.setEmail(userRecord.getEmail());
                        newAgent.setName(userRecord.getDisplayName());
                        newAgent.setPictureUrl(userRecord.getPhotoUrl());
                        return agentRepository.save(newAgent);
                    } catch (FirebaseAuthException e) {
                        throw new RuntimeException("User not found in Firebase", e);
                    }
                });
        return agentMapper.toRecord(agent);
    }

    @Override
    @Transactional
    public AgentRecord bindLineUserToAgent(LineTokenPayload lineTokenPayload) {
        String firebaseUid = SecurityUtils.getCurrentUserUid();
        Agent agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setLineUserId(lineTokenPayload.sub());
        if (agent.getEmail() == null) {
            agent.setEmail(lineTokenPayload.email());
        }
        Agent savedAgent = agentRepository.save(agent);
        return agentMapper.toRecord(savedAgent);
    }

    @Override
    @Transactional
    public AgentRecord updateAgent(String id, UpdateAgentReq req) throws FirebaseAuthException {
        Agent agent = agentRepository.findById(java.util.UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setName(req.displayName());
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(agent.getFirebaseUid())
                .setDisplayName(req.displayName());
        firebaseAuth.updateUser(request);
        Agent updatedAgent = agentRepository.save(agent);
        return agentMapper.toRecord(updatedAgent);
    }

    @Override
    @Transactional
    public void deleteAgent() throws FirebaseAuthException {
        String firebaseUid = SecurityUtils.getCurrentUserUid();
        Agent agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agentRepository.delete(agent);
        firebaseAuth.deleteUser(firebaseUid);
    }
}

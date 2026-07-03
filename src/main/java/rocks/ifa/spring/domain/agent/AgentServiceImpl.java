package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.agent.dtos.*;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.domain.line.LineTokenPayload;
import rocks.ifa.spring.infrastructure.security.SecurityUtils;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final FirebaseAuth firebaseAuth;
    private final AgentRepository agentRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final AgentMapper agentMapper;

    @Override
    @Transactional
    public AgentRes bindLineUserToAgent(LineTokenPayload lineTokenPayload) {
        String firebaseUid = SecurityUtils.getCurrentUserUid();
        String lineUserId = lineTokenPayload.sub();
        String email = lineTokenPayload.email();

        Optional<AgentEntity> agentByFirebase = agentRepository.findByFirebaseUid(firebaseUid);
        Optional<AgentEntity> agentByLine = agentRepository.findByLineUserId(lineUserId);

        AgentEntity agent;
        if (agentByFirebase.isPresent()) {
            agent = agentByFirebase.get();
            if (agent.getLineUserId() != null && !agent.getLineUserId().equals(lineUserId)) {
                throw new IllegalStateException("Agent is already bound to a different LINE user.");
            }
            agent.setLineUserId(lineUserId);
            agent.setName(lineTokenPayload.name());
            agent.setPictureUrl(lineTokenPayload.picture());
            if (agent.getEmail() == null && email != null) {
                agent.setEmail(email);
            }
        } else if (agentByLine.isPresent()) {
            agent = agentByLine.get();
            if (agent.getFirebaseUid() != null && !agent.getFirebaseUid().equals(firebaseUid)) {
                throw new IllegalStateException("Agent is already bound to a different Firebase user.");
            }
            agent.setFirebaseUid(firebaseUid);
        } else {
            agent = new AgentEntity(null, firebaseUid, lineUserId, email, lineTokenPayload.name(), lineTokenPayload.picture());
        }

        AgentEntity savedAgent = agentRepository.save(agent);
        return agentMapper.toAgentRes(savedAgent);
    }
    
    @Override
    public AgentRes getAgent(String agentId) {
        AgentEntity agent = agentRepository.findById(UUID.fromString(agentId))
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));
        return agentMapper.toAgentRes(agent);
    }

    @Override
    public AgentRes getAgentByFirebaseUid(String firebaseUid) {
        AgentEntity agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("Agent not found with firebase uid: " + firebaseUid));
        return agentMapper.toAgentRes(agent);
    }

    @Override
    public AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException {
        AgentEntity agent = agentRepository.findById(UUID.fromString(agentId))
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(agent.getFirebaseUid())
                .setDisplayName(req.displayName());
        firebaseAuth.updateUser(request);

        agent.setName(req.displayName());
        AgentEntity updatedAgent = agentRepository.save(agent);

        log.info("Successfully updated agent: {}", updatedAgent.getFirebaseUid());
        return agentMapper.toAgentRes(updatedAgent);
    }

    @Override
    @Transactional
    public void deleteAgent() throws FirebaseAuthException {
        String agentId = SecurityUtils.getCurrentUserUid();
        log.info("--- Starting self-deletion process for user: {} ---", agentId);

        AgentEntity agent = agentRepository.findByFirebaseUid(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found for firebase uid: " + agentId));

        log.info("Step 1a: Deleting client profiles where agent_firebase_uid is {}", agentId);
        clientProfileRepository.deleteByAgentFirebaseUid(agentId);
        log.info("✅ Completed deleting profiles owned by user.");

        log.info("Step 1b: Deleting client profile where client_firebase_uid is {}", agentId);
        clientProfileRepository.deleteByClientFirebaseUid(agentId);
        log.info("✅ Completed deleting self-profile for user.");

        agentRepository.delete(agent);
        log.info("✅ Completed deleting local agent record.");

        firebaseAuth.deleteUser(agentId);
        log.info("✅ --- Successfully deleted user {} from all systems. ---", agentId);
    }

    @Override
    public UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture) {
        try {
            return firebaseAuth.getUser(lineUserId);
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND) {
                log.info("User with LINE UID {} not found. Creating a new user.", lineUserId);
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setUid(lineUserId)
                        .setDisplayName(name)
                        .setPhotoUrl(picture);
                try {
                    return firebaseAuth.createUser(request);
                } catch (FirebaseAuthException ex) {
                    throw new RuntimeException("Failed to create Firebase user for LINE login", ex);
                }
            }
            throw new RuntimeException(e);
        }
    }
}

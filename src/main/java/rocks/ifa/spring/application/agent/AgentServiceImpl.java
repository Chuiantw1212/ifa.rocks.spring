package rocks.ifa.spring.application.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.application.agent.dto.AgentRes;
import rocks.ifa.spring.application.agent.dto.UpdateAgentReq;
import rocks.ifa.spring.domain.agent.AgentEntity;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.domain.line.LineTokenPayload;
import rocks.ifa.spring.infrastructure.config.SecurityUtils;

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

        // Find agent by current authenticated user (Firebase) or by the LINE ID from the token
        AgentEntity agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> agentRepository.findByLineUserId(lineUserId)
                        .orElse(AgentEntity.createWithFirebase(firebaseUid, lineTokenPayload.email(), null, null)));
        
        // Delegate business logic to the aggregate root
        agent.linkFirebaseAccount(firebaseUid);
        agent.linkLineAccount(lineUserId, lineTokenPayload.email(), lineTokenPayload.name(), lineTokenPayload.picture());

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
    @Transactional
    public AgentRes getAgentByFirebaseUid(String firebaseUid) {
        // Implements "Just-in-Time Provisioning"
        AgentEntity agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> {
                    log.warn("Agent with firebase uid {} not found in local DB. Provisioning now...", firebaseUid);
                    try {
                        UserRecord userRecord = firebaseAuth.getUser(firebaseUid);
                        // Use the factory method on the entity
                        AgentEntity newAgent = AgentEntity.createWithFirebase(
                            userRecord.getUid(),
                            userRecord.getEmail(),
                            userRecord.getDisplayName(),
                            userRecord.getPhotoUrl()
                        );
                        return agentRepository.save(newAgent);
                    } catch (FirebaseAuthException e) {
                        log.error("Failed to fetch user from Firebase while provisioning: {}", firebaseUid, e);
                        throw new IllegalStateException("User not found in Firebase, cannot provision local agent.", e);
                    }
                });

        return agentMapper.toAgentRes(agent);
    }

    @Override
    @Transactional
    public AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException {
        AgentEntity agent = agentRepository.findById(UUID.fromString(agentId))
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

        // Update Firebase record first
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(agent.getFirebaseUid())
                .setDisplayName(req.displayName());
        firebaseAuth.updateUser(request);

        // Delegate profile update to the entity
        agent.updateProfile(req.displayName(), null); // Assuming pictureUrl is not updated here
        
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
    @Transactional
    public UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture) {
        // First, check if an agent with this LINE ID already exists in our local database.
        Optional<AgentEntity> existingAgent = agentRepository.findByLineUserId(lineUserId);

        if (existingAgent.isPresent()) {
            try {
                return firebaseAuth.getUser(lineUserId);
            } catch (FirebaseAuthException e) {
                if (e.getAuthErrorCode() == com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND) {
                    log.warn("Local agent found for LINE UID {} but Firebase user not found. Creating Firebase user.", lineUserId);
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setUid(lineUserId)
                            .setDisplayName(name)
                            .setPhotoUrl(picture);
                    try {
                        return firebaseAuth.createUser(request);
                    } catch (FirebaseAuthException ex) {
                        log.error("Failed to create Firebase user for LINE login: {}", lineUserId, ex);
                        throw new RuntimeException("Failed to create Firebase user for LINE login", ex);
                    }
                }
                log.error("Failed to fetch Firebase user for LINE UID {}: {}", lineUserId, e.getMessage());
                throw new RuntimeException("Failed to fetch Firebase user for LINE login", e);
            }
        } else {
            try {
                UserRecord firebaseUser = firebaseAuth.getUser(lineUserId);
                log.warn("Firebase user found for LINE UID {} but no local agent. Provisioning local agent.", lineUserId);
                AgentEntity newAgent = AgentEntity.createWithFirebase(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl());
                newAgent.linkLineAccount(lineUserId, firebaseUser.getEmail(), name, picture);
                agentRepository.save(newAgent);
                return firebaseUser;
            } catch (FirebaseAuthException e) {
                if (e.getAuthErrorCode() == com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND) {
                    log.info("User with LINE UID {} not found in Firebase. Creating new Firebase user and local agent.", lineUserId);
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setUid(lineUserId)
                            .setDisplayName(name)
                            .setPhotoUrl(picture);
                    try {
                        UserRecord newUser = firebaseAuth.createUser(request);
                        AgentEntity newAgent = AgentEntity.createWithFirebase(newUser.getUid(), null, name, picture);
                        newAgent.linkLineAccount(lineUserId, null, name, picture);
                        agentRepository.save(newAgent);
                        return newUser;
                    } catch (FirebaseAuthException ex) {
                        log.error("Failed to create Firebase user for LINE login: {}", lineUserId, ex);
                        throw new RuntimeException("Failed to create Firebase user for LINE login", ex);
                    }
                }
                log.error("Failed to fetch Firebase user for LINE UID {}: {}", lineUserId, e.getMessage());
                throw new RuntimeException("Failed to fetch Firebase user for LINE login", e);
            }
        }
    }
}

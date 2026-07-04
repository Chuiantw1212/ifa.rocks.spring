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
        // Corrected: Only one set of declarations
        String firebaseUid = SecurityUtils.getCurrentUserUid();
        String lineUserId = lineTokenPayload.sub();
        String email = lineTokenPayload.email();
        String name = lineTokenPayload.name();
        String picture = lineTokenPayload.picture();

        // 1. Find agent by Firebase UID (current authenticated user)
        Optional<AgentEntity> agentByFirebase = agentRepository.findByFirebaseUid(firebaseUid);
        // 2. Find agent by LINE User ID (from the token payload)
        Optional<AgentEntity> agentByLine = agentRepository.findByLineUserId(lineUserId);

        AgentEntity agent;

        if (agentByFirebase.isPresent()) {
            // Case 1: Current Firebase user already exists in our DB.
            agent = agentByFirebase.get();
            if (agent.getLineUserId() != null && !agent.getLineUserId().equals(lineUserId)) {
                throw new IllegalStateException("Agent is already bound to a different LINE user.");
            }
            // Link LINE ID to existing Firebase agent
            agent.setLineUserId(lineUserId);
            agent.setName(name);
            agent.setPictureUrl(picture);
            if (agent.getEmail() == null && email != null) {
                agent.setEmail(email);
            }
        } else if (agentByLine.isPresent()) {
            // Case 2: LINE user already exists in our DB, but not linked to current Firebase user.
            agent = agentByLine.get();
            if (agent.getFirebaseUid() != null && !agent.getFirebaseUid().equals(firebaseUid)) {
                throw new IllegalStateException("Agent is already bound to a different Firebase user.");
            }
            // Link Firebase UID to existing LINE agent
            agent.setFirebaseUid(firebaseUid);
            // Update name and picture from LINE if they are more recent or not set
            if (agent.getName() == null || agent.getName().isEmpty()) {
                agent.setName(name);
            }
            if (agent.getPictureUrl() == null || agent.getPictureUrl().isEmpty()) {
                agent.setPictureUrl(picture);
            }
            if (agent.getEmail() == null && email != null) {
                agent.setEmail(email);
            }
        } else {
            // Case 3: Neither Firebase UID nor LINE User ID found in our DB. Create a new agent.
            agent = new AgentEntity(null, firebaseUid, lineUserId, email, name, picture);
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
    @Transactional
    public AgentRes getAgentByFirebaseUid(String firebaseUid) {
        // Implements "Just-in-Time Provisioning"
        AgentEntity agent = agentRepository.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> {
                    log.warn("Agent with firebase uid {} not found in local DB. Provisioning now...", firebaseUid);
                    try {
                        // Fetch the authoritative user record from Firebase
                        UserRecord userRecord = firebaseAuth.getUser(firebaseUid);
                        
                        // Create a new agent entity based on the Firebase record
                        AgentEntity newAgent = new AgentEntity(
                            null,
                            userRecord.getUid(),
                            null, // lineUserId is unknown at this point
                            userRecord.getEmail(), // email can be null
                            userRecord.getDisplayName(),
                            userRecord.getPhotoUrl()
                        );
                        
                        // Save the new agent to our database and return it
                        return agentRepository.save(newAgent);
                    } catch (FirebaseAuthException e) {
                        log.error("Failed to fetch user from Firebase while provisioning: {}", firebaseUid, e);
                        // If we can't even find the user in Firebase, then something is seriously wrong.
                        throw new IllegalStateException("User not found in Firebase, cannot provision local agent.", e);
                    }
                });

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
    @Transactional
    public UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture) {
        // First, check if an agent with this LINE ID already exists in our local database.
        Optional<AgentEntity> existingAgent = agentRepository.findByLineUserId(lineUserId);

        if (existingAgent.isPresent()) {
            // If the agent exists locally, ensure a Firebase user also exists for this LINE ID.
            // This handles cases where a local agent might exist but the Firebase user was deleted or not created yet.
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
            // If no local agent, check Firebase.
            try {
                UserRecord firebaseUser = firebaseAuth.getUser(lineUserId);
                log.warn("Firebase user found for LINE UID {} but no local agent. Provisioning local agent.", lineUserId);
                // Provision local agent based on Firebase user data
                AgentEntity newAgent = new AgentEntity(
                        null,
                        firebaseUser.getUid(),
                        lineUserId,
                        firebaseUser.getEmail(),
                        firebaseUser.getDisplayName(),
                        firebaseUser.getPhotoUrl()
                );
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
                        // Create new local agent
                        AgentEntity newAgent = new AgentEntity(
                                null,
                                newUser.getUid(),
                                lineUserId,
                                null, // Email is not provided by LINE token directly for new Firebase users
                                name,
                                picture
                        );
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

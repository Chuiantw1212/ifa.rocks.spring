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
import rocks.ifa.spring.infrastructure.security.SecurityUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final FirebaseAuth firebaseAuth;
    private final ClientProfileRepository clientProfileRepository;

    @Override
    public AgentRes createAgent(CreateAgentReq req) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(req.email())
                .setPassword(req.password())
                .setDisplayName(req.displayName())
                .setDisabled(false);

        UserRecord userRecord = firebaseAuth.createUser(request);
        log.info("Successfully created new agent: {}", userRecord.getUid());
        return mapToAgentRes(userRecord);
    }

    @Override
    public AgentRes getAgent(String agentId) {
        try {
            UserRecord userRecord = firebaseAuth.getUser(agentId);
            return mapToAgentRes(userRecord);
        } catch (FirebaseAuthException e) {
            // Consider a more specific exception, e.g., a custom NotFoundException
            throw new RuntimeException("Failed to get user with id: " + agentId, e);
        }
    }

    @Override
    public AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(agentId)
                .setDisplayName(req.displayName())
                .setDisabled(req.disabled());

        UserRecord userRecord = firebaseAuth.updateUser(request);
        log.info("Successfully updated agent: {}", userRecord.getUid());
        return mapToAgentRes(userRecord);
    }

    @Override
    @Transactional
    public void deleteAgent() throws FirebaseAuthException {
        String agentId = SecurityUtils.getCurrentUserUid();
        log.info("--- Starting self-deletion process for user: {} ---", agentId);

        // Step 1: Delete all client profiles where this user is the AGENT.
        log.info("Step 1a: Deleting client profiles where agent_firebase_uid is {}", agentId);
        clientProfileRepository.deleteByAgentFirebaseUid(agentId);
        log.info("✅ Completed deleting profiles owned by user.");

        // Step 2: Delete the client profile where this user is the CLIENT.
        log.info("Step 1b: Deleting client profile where client_firebase_uid is {}", agentId);
        clientProfileRepository.deleteByClientFirebaseUid(agentId);
        log.info("✅ Completed deleting self-profile for user.");

        // Step 3: Delete the user from Firebase Authentication.
        log.info("Step 2: Deleting user from Firebase Authentication...");
        firebaseAuth.deleteUser(agentId);
        log.info("✅ --- Successfully deleted user {} from all systems. ---", agentId);
    }

    @Override
    public UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture) {
        try {
            return firebaseAuth.getUser(lineUserId);
        } catch (FirebaseAuthException e) {
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
    }

    private AgentRes mapToAgentRes(UserRecord userRecord) {
        return new AgentRes(
                userRecord.getUid(),
                userRecord.getEmail(),
                userRecord.getDisplayName(),
                userRecord.isDisabled()
        );
    }
}

package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.agent.dtos.*;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.infra.security.SecurityUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final FirebaseAuth firebaseAuth;
    private final AuthService authService;
    private final ClientProfileRepository clientProfileRepository;

    @Override
    public AuthRes login(LoginReq req) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(req.firebaseToken());
            UserRecord userRecord = firebaseAuth.getUser(decodedToken.getUid());
            log.info("Successfully verified Firebase ID token for user: {}", userRecord.getEmail());
            return authService.handlePostLogin(userRecord);
        } catch (FirebaseAuthException e) {
            log.error("❌ Firebase ID token verification failed", e);
            throw new RuntimeException("Invalid Firebase token", e);
        }
    }

    @Override
    public void logout(String agentId) {
        log.info("Agent logged out: {}", agentId);
    }

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
    public AgentRes getAgent(String agentId) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUser(agentId);
        return mapToAgentRes(userRecord);
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

    private AgentRes mapToAgentRes(UserRecord userRecord) {
        return new AgentRes(
                userRecord.getUid(),
                userRecord.getEmail(),
                userRecord.getDisplayName(),
                userRecord.isDisabled()
        );
    }
}

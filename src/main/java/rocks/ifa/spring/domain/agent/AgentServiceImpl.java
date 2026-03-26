package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.agent.contracts.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService { // Corrected: Implements the standalone AgentService interface

    private final FirebaseAuth firebaseAuth;

    @Override
    public AuthRes login(LoginReq req) {
        log.info("Agent login attempt with token: {}", req.firebaseToken());
        // Dummy implementation
        return new AuthRes("dummy-session-token", null);
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
    public void deleteAgent(String agentId) throws FirebaseAuthException {
        firebaseAuth.deleteUser(agentId);
        log.info("Successfully deleted agent: {}", agentId);
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

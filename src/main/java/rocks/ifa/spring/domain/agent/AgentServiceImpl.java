package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final FirebaseAuth firebaseAuth;

    @Override
    public AgentContracts.AuthRes login(AgentContracts.LoginReq req) {
        log.info("Agent login attempt with token: {}", req.firebaseToken());
        // Dummy implementation
        return new AgentContracts.AuthRes("dummy-session-token", null);
    }

    @Override
    public void logout(String agentId) {
        log.info("Agent logged out: {}", agentId);
    }

    @Override
    public AgentContracts.AgentRes createAgent(AgentContracts.CreateAgentReq req) throws FirebaseAuthException {
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
    public AgentContracts.AgentRes getAgent(String agentId) throws FirebaseAuthException {
        UserRecord userRecord = firebaseAuth.getUser(agentId);
        return mapToAgentRes(userRecord);
    }

    @Override
    public Page<AgentContracts.AgentRes> listAgents(Pageable pageable) {
        log.warn("Paging for listAgents is not fully implemented.");
        return Page.empty(pageable);
    }

    @Override
    public AgentContracts.AgentRes updateAgent(String agentId, AgentContracts.UpdateAgentReq req) throws FirebaseAuthException {
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

    private AgentContracts.AgentRes mapToAgentRes(UserRecord userRecord) {
        return new AgentContracts.AgentRes(
                userRecord.getUid(),
                userRecord.getEmail(),
                userRecord.getDisplayName(),
                userRecord.isDisabled(),
                Instant.ofEpochMilli(userRecord.getUserMetaData().getCreationTimestamp()).atOffset(ZoneOffset.UTC),
                Instant.ofEpochMilli(userRecord.getUserMetaData().getLastSignInTimestamp()).atOffset(ZoneOffset.UTC)
        );
    }
}

package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.agent.contracts.AgentRes;
import rocks.ifa.spring.domain.agent.contracts.AuthRes;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientProfileRepository clientProfileRepository;
    private final FirebaseAuth firebaseAuth;

    @Override
    @Transactional
    public AuthRes handlePostLogin(UserRecord userRecord) {
        String email = userRecord.getEmail();
        if (email == null || email.isBlank()) {
            log.warn("User {} logged in without an email address. Cannot perform client binding.", userRecord.getUid());
            // Decide how to handle users without email. For now, treat as agent.
            return createCustomAuthRes(userRecord, mapToAgentRes(userRecord));
        }

        var clientProfileOpt = clientProfileRepository.findByEmail(email);

        if (clientProfileOpt.isPresent()) {
            // User is a Client
            log.info("User with email {} identified as a Client.", email);
            ClientProfileEntity clientProfile = clientProfileOpt.get();
            if (clientProfile.getClientFirebaseUid() == null) {
                clientProfile.setClientFirebaseUid(userRecord.getUid());
                clientProfileRepository.save(clientProfile);
                log.info("✅ Successfully bound Firebase UID {} to client profile.", userRecord.getUid());
            }
            return createCustomAuthRes(userRecord, null); // Return AuthRes with null AgentRes
        } else {
            // User is an Agent
            log.info("User with email {} identified as an Agent.", email);
            return createCustomAuthRes(userRecord, mapToAgentRes(userRecord));
        }
    }

    private AuthRes createCustomAuthRes(UserRecord userRecord, AgentRes agentRes) {
        try {
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            log.info("✅ Successfully created Firebase custom token for UID: {}", userRecord.getUid());
            return new AuthRes(customToken, agentRes);
        } catch (FirebaseAuthException e) {
            log.error("❌ Failed to create Firebase custom token for UID: {}", userRecord.getUid(), e);
            throw new RuntimeException("Failed to create custom token", e);
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

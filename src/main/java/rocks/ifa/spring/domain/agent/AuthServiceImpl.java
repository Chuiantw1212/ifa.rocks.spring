package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.agent.dtos.AgentRes;
import rocks.ifa.spring.domain.agent.dtos.AuthRes;
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
        log.info("--- Starting Post-Login Handler for Firebase UID: {} ---", userRecord.getUid());
        String email = userRecord.getEmail();

        if (email == null || email.isBlank()) {
            log.warn("⚠️ User {} has no email. Cannot perform client binding. Treating as Agent.", userRecord.getUid());
            return createCustomAuthRes(userRecord, mapToAgentRes(userRecord));
        }
        log.info("User has email: {}. Proceeding with role identification.", email);

        log.info("Step 1: Searching for client profile with email: {}", email);
        var clientProfileOpt = clientProfileRepository.findByEmail(email);

        if (clientProfileOpt.isPresent()) {
            ClientProfileEntity clientProfile = clientProfileOpt.get();
            log.info("✅ Step 2: Found matching client profile (ID: {}). User is identified as a CLIENT.", clientProfile.getId());

            if (clientProfile.getClientFirebaseUid() == null) {
                log.info("Step 3: Client profile is not bound. Attempting to bind Firebase UID: {}", userRecord.getUid());
                clientProfile.setClientFirebaseUid(userRecord.getUid());
                clientProfileRepository.save(clientProfile);
                log.info("✅ Step 4: Successfully bound Firebase UID to client profile.");
            } else {
                log.info("Step 3: Client profile is already bound to UID: {}. No binding action needed.", clientProfile.getClientFirebaseUid());
                if (!clientProfile.getClientFirebaseUid().equals(userRecord.getUid())) {
                    log.warn("⚠️ Mismatch! Profile email {} is bound to UID {}, but current user's UID is {}.", email, clientProfile.getClientFirebaseUid(), userRecord.getUid());
                }
            }
            return createCustomAuthRes(userRecord, null);
        } else {
            log.info("✅ Step 2: No client profile found for email. User is identified as an AGENT.");
            return createCustomAuthRes(userRecord, mapToAgentRes(userRecord));
        }
    }

    private AuthRes createCustomAuthRes(UserRecord userRecord, AgentRes agentRes) {
        try {
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            log.info("Step 5: Successfully created Firebase custom token for UID: {}", userRecord.getUid());
            log.info("--- Post-Login Handler Finished ---");
            return new AuthRes(customToken, agentRes);
        } catch (FirebaseAuthException e) {
            log.error("❌ Step 5: Failed to create Firebase custom token for UID: {}", userRecord.getUid(), e);
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

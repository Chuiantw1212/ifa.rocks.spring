package rocks.ifa.spring.application.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse; // Corrected import
import rocks.ifa.spring.application.auth.dto.FirebaseLoginReq;
import rocks.ifa.spring.application.auth.dto.LineLoginReq;
import rocks.ifa.spring.application.auth.port.LineAuthPort;
import rocks.ifa.spring.domain.agent.AgentEntity;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.domain.line.LineTokenPayload;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final FirebaseAuth firebaseAuth;
    private final AgentRepository agentRepository;
    private final LineAuthPort lineAuthPort;

    @Override
    @Transactional
    public AuthResponse handleFirebaseLogin(FirebaseLoginReq req) throws FirebaseAuthException {
        // This logic remains the same, as it handles both login and registration on desktop
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(req.idToken());
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();
        String picture = decodedToken.getPicture();

        Optional<AgentEntity> agentByUid = agentRepository.findByFirebaseUid(uid);
        if (agentByUid.isPresent()) {
            log.info("Found agent by Firebase UID: {}", uid);
            return createAuthResponse(agentByUid.get());
        }

        if (email != null) {
            Optional<AgentEntity> agentByEmail = agentRepository.findByEmail(email);
            if (agentByEmail.isPresent()) {
                AgentEntity existingAgent = agentByEmail.get();
                log.info("Found agent by email: {}. Checking for account linking.", email);
                if (existingAgent.getFirebaseUid() != null && !existingAgent.getFirebaseUid().equals(uid)) {
                    log.warn("Account with email {} is already linked to a different Firebase UID ({}). Cannot link to new UID ({}).",
                            email, existingAgent.getFirebaseUid(), uid);
                    return createAuthResponse(existingAgent);
                }
                existingAgent.setFirebaseUid(uid);
                if (!StringUtils.hasText(existingAgent.getName())) existingAgent.setName(name);
                if (!StringUtils.hasText(existingAgent.getPictureUrl())) existingAgent.setPictureUrl(picture);
                AgentEntity updatedAgent = agentRepository.save(existingAgent);
                log.info("Successfully linked Firebase UID {} to existing agent with email {}.", uid, email);
                return createAuthResponse(updatedAgent);
            }
        }

        log.info("Creating a new agent for Firebase user {}", uid);
        AgentEntity newAgent = new AgentEntity(null, uid, null, email, name, picture);
        AgentEntity savedAgent = agentRepository.save(newAgent);
        return createAuthResponse(savedAgent);
    }

    @Override
    @Transactional
    public AuthResponse handleLineLogin(LineLoginReq req) throws FirebaseAuthException {
        // 1. Verify LINE ID Token
        LineTokenPayload lineTokenPayload = lineAuthPort.verifyIdToken(req.idToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or unverifiable LINE ID Token."));

        String email = lineTokenPayload.email();
        if (!StringUtils.hasText(email)) {
            log.warn("LINE login attempt without an email. Cannot proceed.");
            return AuthResponse.userNotFound("LINE account did not provide an email address. Please register using a different method.");
        }

        // 2. Find user in Firebase by email
        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);
            log.info("Successfully found Firebase user with email: {}", email);

            // 3. (Optional but recommended) Sync local agent database
            // This ensures that if the user was created via Firebase login first,
            // their LINE user ID gets linked on their first LINE login.
            AgentEntity agent = agentRepository.findByFirebaseUid(userRecord.getUid()).orElseGet(() ->
                agentRepository.findByEmail(email).orElseGet(() -> {
                    log.warn("Firebase user {} exists, but no corresponding agent record found. Creating one now.", userRecord.getUid());
                    AgentEntity newAgent = new AgentEntity(null, userRecord.getUid(), lineTokenPayload.sub(), email, userRecord.getDisplayName(), userRecord.getPhotoUrl());
                    return agentRepository.save(newAgent);
                })
            );

            if (agent.getLineUserId() == null) {
                agent.setLineUserId(lineTokenPayload.sub());
                agentRepository.save(agent);
                log.info("Linked LINE User ID {} to existing agent.", lineTokenPayload.sub());
            }

            // 4. Generate and return Firebase Custom Token
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            return AuthResponse.success(customToken);

        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND) {
                // 5. If user does not exist in Firebase, return USER_NOT_FOUND status
                log.info("User with email {} not found in Firebase.", email);
                return AuthResponse.userNotFound("User with this email is not registered. Please sign up first.");
            }
            // Re-throw other Firebase exceptions
            throw e;
        }
    }

    @Override
    public void logout() {
        log.info("User logout requested. Client should clear its token.");
    }

    private AuthResponse createAuthResponse(AgentEntity agent) throws FirebaseAuthException {
        String customToken = firebaseAuth.createCustomToken(agent.getFirebaseUid(), Map.of("agentId", agent.getId().toString()));
        return AuthResponse.success(customToken);
    }
}

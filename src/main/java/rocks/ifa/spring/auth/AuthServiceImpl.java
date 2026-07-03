package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.auth.dtos.FirebaseLoginReq;
import rocks.ifa.spring.domain.agent.AgentEntity;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final FirebaseAuth firebaseAuth;
    private final AgentRepository agentRepository;

    @Override
    @Transactional
    public AuthResponse handleFirebaseLogin(FirebaseLoginReq req) throws FirebaseAuthException {
        // 1. Verify Firebase ID Token
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(req.idToken());
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();
        String picture = decodedToken.getPicture();

        // 2. Find or Create Agent
        AgentEntity agent = agentRepository.findByFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("Agent with Firebase UID {} not found. Checking by email...", uid);
                    // If not found by UID, try to find by email to link accounts
                    if (email != null) {
                        Optional<AgentEntity> agentByEmail = agentRepository.findByEmail(email);
                        if (agentByEmail.isPresent()) {
                            log.info("Found agent by email {}. Linking Firebase UID {}.", email, uid);
                            AgentEntity existingAgent = agentByEmail.get();
                            existingAgent.setFirebaseUid(uid); // Link the new Firebase UID
                            return agentRepository.save(existingAgent);
                        }
                    }
                    // If still not found, create a new agent
                    log.info("Creating a new agent for Firebase user {}", uid);
                    AgentEntity newAgent = new AgentEntity(null, uid, null, email, name, picture);
                    return agentRepository.save(newAgent);
                });

        // 3. Create a custom token to consolidate session
        String customToken = firebaseAuth.createCustomToken(agent.getFirebaseUid(), Map.of("agentId", agent.getId().toString()));
        return AuthResponse.success(customToken);
    }

    @Override
    @Transactional
    public AuthResponse handleLineLogin(LineTokenPayload lineTokenPayload) throws FirebaseAuthException {
        String lineUserId = lineTokenPayload.sub();
        String email = lineTokenPayload.email();

        // 1. Find user by lineUserId
        Optional<AgentEntity> agentByLineId = agentRepository.findByLineUserId(lineUserId);
        if (agentByLineId.isPresent()) {
            log.info("Found agent by LINE User ID: {}", lineUserId);
            AgentEntity agent = agentByLineId.get();
            String firebaseUid = ensureFirebaseUser(agent.getFirebaseUid() != null ? agent.getFirebaseUid() : lineUserId, email, lineTokenPayload.name(), lineTokenPayload.picture());
            if (agent.getFirebaseUid() == null) {
                agent.setFirebaseUid(firebaseUid);
                agentRepository.save(agent);
            }
            String customToken = firebaseAuth.createCustomToken(firebaseUid);
            return AuthResponse.success(customToken);
        }

        // 2. If not found by lineUserId, check by email
        if (email != null) {
            Optional<AgentEntity> agentByEmail = agentRepository.findByEmail(email);
            if (agentByEmail.isPresent()) {
                log.warn("LINE login attempt with email {} that already exists in the system.", email);
                return AuthResponse.accountExists("An account with this email already exists. Please log in with your original method and link your LINE account from the settings.");
            }
        }

        // 3. Truly a new user, create both local and Firebase accounts
        log.info("Creating a new agent for LINE user {}", lineUserId);
        String firebaseUid = ensureFirebaseUser(lineUserId, email, lineTokenPayload.name(), lineTokenPayload.picture());
        AgentEntity newAgent = new AgentEntity(null, firebaseUid, lineUserId, email, lineTokenPayload.name(), lineTokenPayload.picture());
        agentRepository.save(newAgent);

        String customToken = firebaseAuth.createCustomToken(firebaseUid);
        return AuthResponse.success(customToken);
    }

    @Override
    public void logout() {
        log.info("User logout requested. Client should clear its token.");
    }

    private String ensureFirebaseUser(String uid, String email, String name, String picture) throws FirebaseAuthException {
        try {
            UserRecord userRecord = firebaseAuth.getUser(uid);
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND) {
                log.info("User {} not found in Firebase. Creating a new Firebase user.", uid);
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setUid(uid)
                        .setEmail(email)
                        .setDisplayName(name)
                        .setPhotoUrl(picture);
                UserRecord newUser = firebaseAuth.createUser(request);
                return newUser.getUid();
            }
            throw e;
        }
    }
}

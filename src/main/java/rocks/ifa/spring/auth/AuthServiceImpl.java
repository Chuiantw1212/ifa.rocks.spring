package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import rocks.ifa.spring.auth.dtos.FirebaseLoginReq;
import rocks.ifa.spring.auth.dtos.LineLoginReq;
import rocks.ifa.spring.domain.agent.AgentEntity;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;
import rocks.ifa.spring.auth.port.LineAuthPort;


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
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(req.idToken());
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = decodedToken.getName();
        String picture = decodedToken.getPicture();

        AgentEntity agent = agentRepository.findByFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("Agent with Firebase UID {} not found. Checking by email...", uid);
                    if (email != null) {
                        Optional<AgentEntity> agentByEmail = agentRepository.findByEmail(email);
                        if (agentByEmail.isPresent()) {
                            log.info("Found agent by email {}. Linking Firebase UID {}.", email, uid);
                            AgentEntity existingAgent = agentByEmail.get();
                            existingAgent.setFirebaseUid(uid);
                            return agentRepository.save(existingAgent);
                        }
                    }
                    log.info("Creating a new agent for Firebase user {}", uid);
                    AgentEntity newAgent = new AgentEntity(null, uid, null, email, name, picture);
                    return agentRepository.save(newAgent);
                });

        String customToken = firebaseAuth.createCustomToken(agent.getFirebaseUid(), Map.of("agentId", agent.getId().toString()));
        return AuthResponse.success(customToken);
    }

    @Override
    @Transactional
    public AuthResponse handleLineLogin(LineLoginReq req) throws FirebaseAuthException {
        LineTokenPayload lineTokenPayload = lineAuthPort.verifyIdToken(req.idToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or unverifiable LINE ID Token."));

        String lineUserId = lineTokenPayload.sub();
        String email = lineTokenPayload.email();

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

        if (email != null) {
            Optional<AgentEntity> agentByEmail = agentRepository.findByEmail(email);
            if (agentByEmail.isPresent()) {
                log.warn("LINE login attempt with email {} that already exists in the system.", email);
                return AuthResponse.accountExists("An account with this email already exists. Please log in with your original method and link your LINE account from the settings.");
            }
        }

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
                UserRecord.CreateRequest request = new UserRecord.CreateRequest().setUid(uid);

                // Only set email if it has a valid value
                if (StringUtils.hasText(email)) {
                    request.setEmail(email);
                }
                
                request.setDisplayName(name);
                request.setPhotoUrl(picture);

                UserRecord newUser = firebaseAuth.createUser(request);
                return newUser.getUid();
            }
            throw e;
        }
    }
}

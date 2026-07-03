package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.auth.dtos.AuthRes;
import rocks.ifa.spring.auth.dtos.FirebaseCustomToken;
import rocks.ifa.spring.auth.dtos.LiffIdToken;
import rocks.ifa.spring.auth.dtos.LoginReq;
import rocks.ifa.spring.auth.port.LineAuthPort;
import rocks.ifa.spring.domain.agent.AgentEntity;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final FirebaseAuth firebaseAuth;
    private final AgentRepository agentRepository; // Injected AgentRepository
    private final LineAuthPort lineAuthPort;

    @Override
    @Transactional
    public AuthResponse loginWithLine(LineTokenPayload lineTokenPayload) throws FirebaseAuthException {
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

    // --- Existing methods ---
    @Override
    public AuthRes loginWithFirebase(LoginReq req) {
        // This method might need adjustment if AgentService is fully removed.
        // For now, it's kept as is, but might need to inject AgentMapper.
        throw new UnsupportedOperationException("loginWithFirebase needs to be refactored without AgentService.");
    }

    @Override
    public FirebaseCustomToken loginWithLiff(LiffIdToken idToken) {
        // This method also needs refactoring to use AgentRepository directly.
        throw new UnsupportedOperationException("loginWithLiff needs to be refactored without AgentService.");
    }

    @Override
    public void logout() {
        log.info("User logout requested.");
    }
}

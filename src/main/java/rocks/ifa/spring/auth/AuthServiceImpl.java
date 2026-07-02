package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rocks.ifa.spring.domain.agent.AgentService; // Corrected import
import rocks.ifa.spring.auth.dtos.AuthRes;
import rocks.ifa.spring.auth.dtos.LoginReq;
import rocks.ifa.spring.auth.dtos.FirebaseCustomToken;
import rocks.ifa.spring.auth.dtos.LiffIdToken;
import rocks.ifa.spring.auth.dtos.LineTokenPayload; // Corrected import
import rocks.ifa.spring.auth.port.LineAuthPort;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final FirebaseAuth firebaseAuth;
    private final AgentService agentService;
    private final LineAuthPort lineAuthPort;

    @Value("${line.liff.channel-id}")
    private String lineLiffChannelId;

    @Override
    public AuthRes loginWithFirebase(LoginReq req) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(req.firebaseToken());
            UserRecord userRecord = firebaseAuth.getUser(decodedToken.getUid());
            log.info("Successfully verified Firebase ID token for user: {}", userRecord.getEmail());

            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            return new AuthRes(customToken, agentService.getAgent(userRecord.getUid()));

        } catch (FirebaseAuthException e) {
            log.error("❌ Firebase ID token verification failed", e);
            throw new RuntimeException("Invalid Firebase token", e);
        }
    }

    @Override
    public FirebaseCustomToken loginWithLiff(LiffIdToken idToken) {
        // 1. Verify ID Token with LINE
        LineTokenPayload payload = lineAuthPort.verifyIdToken(idToken.idToken(), lineLiffChannelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID Token"));

        // 2. Validate Payload
        payload.validate(lineLiffChannelId);

        // 3. Find or create user in Agent domain
        String lineUserId = "line:" + payload.sub();
        UserRecord userRecord = agentService.findOrCreateAgentByLineId(lineUserId, payload.name(), payload.picture());

        // 4. Create Firebase Custom Token
        try {
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            return new FirebaseCustomToken(customToken);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Failed to create Firebase custom token", e);
        }
    }

    @Override
    public void logout() {
        // The actual logout mechanism (e.g., token revocation) would be implemented here.
        // For now, we just log the action.
        log.info("User logout requested.");
    }
}

package rocks.ifa.spring.domain.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rocks.ifa.spring.domain.agent.contracts.AuthRes;
import rocks.ifa.spring.domain.agent.contracts.LiffLoginReq;
import rocks.ifa.spring.infra.config.LineLiffProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiffAuthServiceImpl implements LiffAuthService {

    private final FirebaseAuth firebaseAuth;
    private final RestTemplate restTemplate;
    private final LineLiffProperties lineLiffProperties;

    private static final String LINE_VERIFY_URL = "https://api.line.me/oauth2/v2.1/verify";

    @Override
    public AuthRes loginWithLiff(LiffLoginReq req) {
        // 1. Verify LIFF ID Token with LINE Platform
        String lineUserId = verifyLiffToken(req.token());

        // 2. Find or create a user in Firebase Auth based on the LINE User ID
        UserRecord userRecord = getOrCreateFirebaseUser(lineUserId);

        // 3. Create a custom token for the Firebase user
        try {
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            log.info("✅ Successfully created Firebase custom token for LINE user: {}", lineUserId);
            // Note: AgentRes is not directly part of LiffAuthService's responsibility,
            // but it's needed for AuthRes. We'll create a minimal one here.
            // In a real app, you might fetch full agent details from AgentService.
            return new AuthRes(customToken, new AgentRes(userRecord.getUid(), userRecord.getEmail(), userRecord.getDisplayName(), userRecord.isDisabled()));
        } catch (FirebaseAuthException e) {
            log.error("❌ Failed to create Firebase custom token for UID: {}", userRecord.getUid(), e);
            throw new RuntimeException("Failed to create custom token", e);
        }
    }

    private String verifyLiffToken(String liffIdToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id_token", liffIdToken);
        map.add("client_id", lineLiffProperties.getChannelId());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<LineVerifyResponse> response = restTemplate.postForEntity(LINE_VERIFY_URL, entity, LineVerifyResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("✅ LIFF token verified successfully for LINE user: {}", response.getBody().sub());
                return response.getBody().sub();
            } else {
                log.error("❌ LIFF token verification failed with status: {}", response.getStatusCode());
                throw new IllegalArgumentException("Invalid LIFF token");
            }
        } catch (Exception e) {
            log.error("❌ Error while verifying LIFF token", e);
            throw new IllegalArgumentException("Invalid LIFF token", e);
        }
    }

    private UserRecord getOrCreateFirebaseUser(String lineUserId) {
        try {
            // The UID in Firebase will be prefixed to distinguish from other providers
            String firebaseUid = "line:" + lineUserId;
            return firebaseAuth.getUser(firebaseUid);
        } catch (FirebaseAuthException e) {
            if ("user-not-found".equals(e.getErrorCode())) {
                log.info("User with LINE ID {} not found in Firebase, creating a new user.", lineUserId);
                return createFirebaseUserFromLine(lineUserId);
            }
            throw new RuntimeException("Error fetching Firebase user", e);
        }
    }

    private UserRecord createFirebaseUserFromLine(String lineUserId) {
        String firebaseUid = "line:" + lineUserId;
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setUid(firebaseUid)
                .setDisplayName("LINE User " + lineUserId.substring(0, 6)) // A default display name
                .setDisabled(false);
        try {
            return firebaseAuth.createUser(request);
        } catch (FirebaseAuthException ex) {
            log.error("❌ Failed to create Firebase user for LINE ID: {}", lineUserId, ex);
            throw new RuntimeException("Failed to create Firebase user", ex);
        }
    }
    
    // Helper record for deserializing LINE's verification response
    private record LineVerifyResponse(String sub, String name, String picture) {}
}

package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rocks.ifa.spring.domain.agent.contracts.AgentRes;
import rocks.ifa.spring.domain.agent.contracts.AuthRes;
import rocks.ifa.spring.domain.agent.contracts.LiffLoginReq;
import rocks.ifa.spring.domain.agent.contracts.LineVerifyResponse; // Import the new record
import rocks.ifa.spring.infra.config.LineLiffProperties;

@Slf4j
@Service
public class LiffAuthServiceImpl implements LiffAuthService {

    private final FirebaseAuth firebaseAuth;
    private final WebClient webClient;
    private final LineLiffProperties lineLiffProperties;

    private static final String LINE_VERIFY_URL = "https://api.line.me/oauth2/v2.1/verify";

    public LiffAuthServiceImpl(FirebaseAuth firebaseAuth, WebClient.Builder webClientBuilder, LineLiffProperties lineLiffProperties) {
        this.firebaseAuth = firebaseAuth;
        this.webClient = webClientBuilder.baseUrl(LINE_VERIFY_URL).build();
        this.lineLiffProperties = lineLiffProperties;
    }

    @Override
    public AuthRes loginWithLiff(LiffLoginReq req) {
        String lineUserId = verifyLiffToken(req.token());
        UserRecord userRecord = getOrCreateFirebaseUser(lineUserId);
        try {
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            log.info("✅ Successfully created Firebase custom token for LINE user: {}", lineUserId);
            return new AuthRes(customToken, new AgentRes(userRecord.getUid(), userRecord.getEmail(), userRecord.getDisplayName(), userRecord.isDisabled()));
        } catch (FirebaseAuthException e) {
            log.error("❌ Failed to create Firebase custom token for UID: {}", userRecord.getUid(), e);
            throw new RuntimeException("Failed to create custom token", e);
        }
    }

    private String verifyLiffToken(String liffIdToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id_token", liffIdToken);
        formData.add("client_id", lineLiffProperties.getChannelId());

        LineVerifyResponse response = webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("❌ LIFF token verification failed with status: {} and body: {}", clientResponse.statusCode(), errorBody);
                                    return Mono.error(new IllegalArgumentException("Invalid LIFF token"));
                                })
                )
                .bodyToMono(LineVerifyResponse.class)
                .block();

        if (response != null && response.sub() != null) {
            log.info("✅ LIFF token verified successfully for LINE user: {}", response.sub());
            return response.sub();
        } else {
            throw new IllegalArgumentException("Invalid LIFF token: response or subject is null");
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
}

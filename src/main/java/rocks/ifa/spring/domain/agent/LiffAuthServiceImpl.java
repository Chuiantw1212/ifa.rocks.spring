package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rocks.ifa.spring.domain.agent.dtos.AuthRes;
import rocks.ifa.spring.domain.agent.dtos.LiffLoginReq;
import rocks.ifa.spring.domain.agent.dtos.LineVerifyResponse;
import rocks.ifa.spring.infra.config.LineLiffProperties;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiffAuthServiceImpl implements LiffAuthService {

    private final FirebaseAuth firebaseAuth;
    private final WebClient.Builder webClientBuilder;
    private final LineLiffProperties lineLiffProperties;
    private final AuthService authService;

    private static final String LINE_VERIFY_URL = "https://api.line.me/oauth2/v2.1/verify";

    @Override
    public AuthRes loginWithLiff(LiffLoginReq req) {
        LineVerifyResponse lineUser = verifyLiffToken(req.token());
        UserRecord firebaseUser = getOrCreateFirebaseUser(lineUser);
        return authService.handlePostLogin(firebaseUser);
    }

    private LineVerifyResponse verifyLiffToken(String liffIdToken) {
        WebClient webClient = webClientBuilder.baseUrl(LINE_VERIFY_URL).build();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id_token", liffIdToken);
        formData.add("client_id", lineLiffProperties.getChannelId());

        return webClient.post()
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
    }

    private UserRecord getOrCreateFirebaseUser(LineVerifyResponse lineUser) {
        String email = lineUser.email();
        String displayName = lineUser.name();
        String lineUserId = lineUser.sub();

        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setDisplayName(displayName)
                    .setDisabled(false);
            
            UserRecord newUserRecord = firebaseAuth.createUser(request);
            log.info("✅ Successfully created a new Firebase user with email: {}", email);
            
            firebaseAuth.setCustomUserClaims(newUserRecord.getUid(), Map.of("lineUserId", lineUserId));
            
            return newUserRecord;

        } catch (FirebaseAuthException e) {
            if ("email-already-exists".equals(e.getErrorCode())) {
                log.info("User with email {} already exists. Fetching existing user.", email);
                try {
                    return firebaseAuth.getUserByEmail(email);
                } catch (FirebaseAuthException ex) {
                    log.error("❌ Failed to fetch existing user by email: {}", email, ex);
                    throw new RuntimeException("Failed to fetch existing user", ex);
                }
            } else {
                log.error("❌ An unexpected Firebase error occurred during user creation for email: {}", email, e);
                throw new RuntimeException("Firebase user creation failed", e);
            }
        }
    }
}

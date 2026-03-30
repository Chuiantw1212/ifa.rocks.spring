package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import rocks.ifa.spring.domain.agent.contracts.AgentRes;
import rocks.ifa.spring.domain.agent.contracts.AuthRes;
import rocks.ifa.spring.domain.agent.contracts.LiffLoginReq;
import rocks.ifa.spring.domain.agent.contracts.LineVerifyResponse;
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.infra.config.LineLiffProperties;

@Slf4j
@Service
public class LiffAuthServiceImpl implements LiffAuthService {

    private final FirebaseAuth firebaseAuth;
    private final WebClient webClient;
    private final LineLiffProperties lineLiffProperties;
    private final ClientProfileRepository clientProfileRepository;

    private static final String LINE_VERIFY_URL = "https://api.line.me/oauth2/v2.1/verify";

    public LiffAuthServiceImpl(FirebaseAuth firebaseAuth, WebClient.Builder webClientBuilder, LineLiffProperties lineLiffProperties, ClientProfileRepository clientProfileRepository) {
        this.firebaseAuth = firebaseAuth;
        this.webClient = webClientBuilder.baseUrl(LINE_VERIFY_URL).build();
        this.lineLiffProperties = lineLiffProperties;
        this.clientProfileRepository = clientProfileRepository;
    }

    @Override
    @Transactional
    public AuthRes loginWithLiff(LiffLoginReq req) {
        // 1. Verify LIFF token and get user info from LINE
        LineVerifyResponse lineUser = verifyLiffToken(req.token());
        String lineUserId = lineUser.sub();
        String lineEmail = lineUser.email();

        if (lineEmail == null || lineEmail.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not available from LINE. Please ensure email scope is granted.");
        }

        // 2. Check if the user is a client
        var clientProfileOpt = clientProfileRepository.findByEmail(lineEmail);

        if (clientProfileOpt.isPresent()) {
            // User is a Client
            log.info("User with email {} identified as a Client.", lineEmail);
            ClientProfileEntity clientProfile = clientProfileOpt.get();
            UserRecord firebaseUser = getOrCreateFirebaseUser(lineUserId, lineEmail, lineUser.name());

            if (clientProfile.getClientFirebaseUid() == null) {
                clientProfile.setClientFirebaseUid(firebaseUser.getUid());
                clientProfileRepository.save(clientProfile);
                log.info("✅ Successfully bound Firebase UID {} to client profile.", firebaseUser.getUid());
            }
            return createCustomAuthRes(firebaseUser, null); // Return AuthRes with null AgentRes
        } else {
            // User is an Agent (or new user to be treated as agent)
            log.info("User with email {} identified as an Agent.", lineEmail);
            UserRecord firebaseUser = getOrCreateFirebaseUser(lineUserId, lineEmail, lineUser.name());
            AgentRes agentRes = new AgentRes(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getDisplayName(), firebaseUser.isDisabled());
            return createCustomAuthRes(firebaseUser, agentRes);
        }
    }

    private LineVerifyResponse verifyLiffToken(String liffIdToken) {
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

    private UserRecord getOrCreateFirebaseUser(String lineUserId, String email, String displayName) {
        String firebaseUid = "line:" + lineUserId;
        try {
            return firebaseAuth.getUser(firebaseUid);
        } catch (FirebaseAuthException e) {
            if ("user-not-found".equals(e.getErrorCode())) {
                log.info("User with LINE ID {} not found in Firebase, creating a new user.", lineUserId);
                return createFirebaseUserFromLine(firebaseUid, email, displayName);
            }
            throw new RuntimeException("Error fetching Firebase user", e);
        }
    }

    private UserRecord createFirebaseUserFromLine(String firebaseUid, String email, String displayName) {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setUid(firebaseUid)
                .setEmail(email)
                .setDisplayName(displayName)
                .setDisabled(false);
        try {
            return firebaseAuth.createUser(request);
        } catch (FirebaseAuthException ex) {
            log.error("❌ Failed to create Firebase user for email: {}", email, ex);
            throw new RuntimeException("Failed to create Firebase user", ex);
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
}

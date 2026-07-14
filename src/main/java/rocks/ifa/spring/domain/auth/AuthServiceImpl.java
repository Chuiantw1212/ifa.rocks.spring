package rocks.ifa.spring.domain.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.auth.dtos.FirebaseLoginReq;
import rocks.ifa.spring.domain.auth.dtos.LineLoginReq;
import rocks.ifa.spring.domain.agent.Agent;
import rocks.ifa.spring.domain.agent.AgentRepository;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.auth.dtos.LineTokenPayload;
import rocks.ifa.spring.domain.auth.port.LineAuthPort;

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

        Optional<Agent> agentByUid = agentRepository.findByFirebaseUid(uid);
        if (agentByUid.isPresent()) {
            log.info("Found agent by Firebase UID: {}", uid);
            return createAuthResponse(agentByUid.get());
        }

        if (email != null) {
            Optional<Agent> agentByEmail = agentRepository.findByEmail(email);
            if (agentByEmail.isPresent()) {
                Agent existingAgent = agentByEmail.get();
                existingAgent.linkFirebaseAccount(uid);
                existingAgent.updateProfile(name, picture);
                Agent updatedAgent = agentRepository.save(existingAgent);
                log.info("Successfully linked Firebase UID {} to existing agent with email {}.", uid, email);
                return createAuthResponse(updatedAgent);
            }
        }

        log.info("Creating a new agent for Firebase user {}", uid);
        Agent newAgent = Agent.createWithFirebase(uid, email, name, picture);
        Agent savedAgent = agentRepository.save(newAgent);
        return createAuthResponse(savedAgent);
    }

    @Override
    @Transactional
    public AuthResponse handleLineLogin(LineLoginReq req) throws FirebaseAuthException {
        LineTokenPayload lineTokenPayload = lineAuthPort.verifyIdToken(req.idToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or unverifiable LINE ID Token."));

        String email = lineTokenPayload.email();
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LINE account did not provide an email address.");
        }

        try {
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);
            log.info("Successfully found Firebase user with email: {}", email);
            Agent agent = agentRepository.findByFirebaseUid(userRecord.getUid()).orElseGet(() ->
                agentRepository.findByEmail(email).orElseGet(() -> {
                    Agent newAgent = Agent.createWithFirebase(userRecord.getUid(), email, userRecord.getDisplayName(), userRecord.getPhotoUrl());
                    return agentRepository.save(newAgent);
                })
            );
            agent.linkLineAccount(lineTokenPayload.sub(), lineTokenPayload.email(), lineTokenPayload.name(), lineTokenPayload.picture());
            agentRepository.save(agent);
            String customToken = firebaseAuth.createCustomToken(userRecord.getUid());
            return AuthResponse.success(customToken);
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == com.google.firebase.auth.AuthErrorCode.USER_NOT_FOUND) {
                log.warn("User with email {} not found in Firebase.", email);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email is not registered.");
            }
            throw e;
        }
    }

    @Override
    public void logout() {
        log.info("User logout requested. Client should clear its token.");
    }

    private AuthResponse createAuthResponse(Agent agent) throws FirebaseAuthException {
        String customToken = firebaseAuth.createCustomToken(agent.getFirebaseUid(), Map.of("agentId", agent.getId().toString()));
        return AuthResponse.success(customToken);
    }
}

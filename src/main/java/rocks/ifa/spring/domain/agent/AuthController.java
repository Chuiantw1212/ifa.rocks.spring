package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "Handles user sign-in and registration flows.")
@RequiredArgsConstructor
public class AuthController {

    private final AgentService agentService;

    @Operation(summary = "Sign in with LINE",
               description = "Receives a LINE ID Token, finds or creates a user in both the local database and Firebase, " +
                             "and returns a Firebase Custom Token for the client to sign in.")
    @PostMapping("/login-line")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse loginWithLine(@RequestBody @Valid LineTokenPayload lineTokenPayload) throws FirebaseAuthException {
        return agentService.loginWithLine(lineTokenPayload);
    }
}

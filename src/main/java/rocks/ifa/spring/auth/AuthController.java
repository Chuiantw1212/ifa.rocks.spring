package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.auth.dtos.FirebaseLoginReq;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "Handles user sign-in and sign-up for both LINE (mobile) and Firebase (desktop).")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Firebase Sign-in (Desktop)",
               description = "Handles both sign-in and sign-up via Firebase. The client provides a Firebase ID Token, " +
                             "and the backend finds or creates the corresponding agent.")
    @PostMapping("/firebase")
    public AuthResponse handleFirebaseLogin(@RequestBody @Valid FirebaseLoginReq req) throws FirebaseAuthException {
        return authService.handleFirebaseLogin(req);
    }

    @Operation(summary = "LINE Sign-in (Mobile)",
               description = "Handles both sign-in and sign-up via LINE. The client provides a LINE ID Token, " +
                             "and the backend finds or creates the corresponding agent and returns a Firebase Custom Token.")
    @PostMapping("/line")
    public AuthResponse handleLineLogin(@RequestBody @Valid LineTokenPayload lineTokenPayload) throws FirebaseAuthException {
        return authService.handleLineLogin(lineTokenPayload);
    }

    @Operation(summary = "Sign out the current user")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}

package rocks.ifa.spring.adapter.web;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocks.ifa.spring.application.auth.AuthService;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse; // Corrected import
import rocks.ifa.spring.application.auth.dto.FirebaseLoginReq;
import rocks.ifa.spring.application.auth.dto.LineLoginReq;

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
               description = "Handles both sign-in and sign-up via LINE. The client provides a raw LINE ID Token, " +
                             "the backend verifies it, finds or creates the agent, and returns a Firebase Custom Token.")
    @PostMapping("/line")
    public AuthResponse handleLineLogin(@RequestBody @Valid LineLoginReq req) throws FirebaseAuthException {
        return authService.handleLineLogin(req);
    }

    @Operation(summary = "Sign out the current user")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}

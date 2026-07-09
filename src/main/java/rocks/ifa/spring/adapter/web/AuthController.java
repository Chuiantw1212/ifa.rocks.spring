package rocks.ifa.spring.adapter.web;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.application.auth.AuthService;
import rocks.ifa.spring.application.auth.dto.AuthResponseDTO;
import rocks.ifa.spring.application.auth.dto.FirebaseLoginReq;
import rocks.ifa.spring.application.auth.dto.LineLoginReq;
import rocks.ifa.spring.application.auth.exception.UserNotFoundException;

import java.util.Map;

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
    public ResponseEntity<AuthResponseDTO> handleFirebaseLogin(@RequestBody @Valid FirebaseLoginReq req) throws FirebaseAuthException {
        return ResponseEntity.ok(authService.handleFirebaseLogin(req));
    }

    @Operation(summary = "LINE Sign-in (Mobile)",
               description = "Handles both sign-in and sign-up via LINE. The client provides a raw LINE ID Token, " +
                             "the backend verifies it, finds or creates the agent, and returns a Firebase Custom Token.")
    @PostMapping("/line")
    public ResponseEntity<AuthResponseDTO> handleLineLogin(@RequestBody @Valid LineLoginReq req) throws FirebaseAuthException {
        return ResponseEntity.ok(authService.handleLineLogin(req));
    }

    @Operation(summary = "Sign out the current user")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }
}

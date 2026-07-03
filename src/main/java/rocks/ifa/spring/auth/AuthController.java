package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.ifa.spring.auth.dtos.AuthRes;
import rocks.ifa.spring.auth.dtos.FirebaseCustomToken;
import rocks.ifa.spring.auth.dtos.LiffIdToken;
import rocks.ifa.spring.auth.dtos.LoginReq;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "處理所有使用者登入登出相關的認證服務")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Sign in with LINE",
               description = "Receives a LINE ID Token, finds or creates a user, and returns a Firebase Custom Token.")
    @PostMapping("/login-line")
    public AuthResponse loginWithLine(@RequestBody @Valid LineTokenPayload lineTokenPayload) throws FirebaseAuthException {
        return authService.loginWithLine(lineTokenPayload);
    }

    @Operation(summary = "顧問透過 Firebase 登入")
    @PostMapping("/agent/login")
    public ResponseEntity<AuthRes> loginWithFirebase(@RequestBody @Valid LoginReq req) {
        return ResponseEntity.ok(authService.loginWithFirebase(req));
    }

    @Operation(summary = "使用者透過 LINE LIFF 登入")
    @PostMapping("/liff")
    public FirebaseCustomToken loginWithLiff(@RequestBody LiffIdToken idToken) {
        return authService.loginWithLiff(idToken);
    }

    @Operation(summary = "顧問登出")
    @PostMapping("/agent/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}

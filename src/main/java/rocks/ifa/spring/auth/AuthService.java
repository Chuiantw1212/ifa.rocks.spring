package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.auth.dtos.AuthRes;
import rocks.ifa.spring.auth.dtos.FirebaseCustomToken;
import rocks.ifa.spring.auth.dtos.LiffIdToken;
import rocks.ifa.spring.auth.dtos.LoginReq;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

public interface AuthService {
    AuthRes loginWithFirebase(LoginReq req);
    FirebaseCustomToken loginWithLiff(LiffIdToken idToken);
    void logout();

    // Add the new method for LINE login
    AuthResponse loginWithLine(LineTokenPayload lineTokenPayload) throws FirebaseAuthException;
}

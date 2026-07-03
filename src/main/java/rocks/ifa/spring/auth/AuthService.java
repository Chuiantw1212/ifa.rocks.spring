package rocks.ifa.spring.auth;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.auth.dtos.FirebaseLoginReq;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;
import rocks.ifa.spring.domain.line.LineTokenPayload;

public interface AuthService {

    /**
     * Handles sign-in/sign-up from desktop clients via Firebase.
     * @param req The request containing the Firebase ID Token.
     * @return An authentication response, typically with a custom token.
     * @throws FirebaseAuthException if the ID token is invalid.
     */
    AuthResponse handleFirebaseLogin(FirebaseLoginReq req) throws FirebaseAuthException;

    /**
     * Handles sign-in/sign-up from mobile clients via LINE.
     * @param lineTokenPayload The payload from the LINE ID Token.
     * @return An authentication response with a Firebase Custom Token.
     * @throws FirebaseAuthException if Firebase operations fail.
     */
    AuthResponse handleLineLogin(LineTokenPayload lineTokenPayload) throws FirebaseAuthException;

    /**
     * Logs out the current user.
     */
    void logout();
}

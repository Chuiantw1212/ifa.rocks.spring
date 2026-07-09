package rocks.ifa.spring.application.auth;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse; // Corrected import
import rocks.ifa.spring.application.auth.dto.FirebaseLoginReq;
import rocks.ifa.spring.application.auth.dto.LineLoginReq;

public interface AuthService {

    AuthResponse handleFirebaseLogin(FirebaseLoginReq req) throws FirebaseAuthException;

    AuthResponse handleLineLogin(LineLoginReq req) throws FirebaseAuthException;

    void logout();
}

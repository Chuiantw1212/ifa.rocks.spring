package rocks.ifa.spring.application.auth;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.application.auth.dto.AuthResponseDTO; // Corrected import
import rocks.ifa.spring.application.auth.dto.FirebaseLoginReq;
import rocks.ifa.spring.application.auth.dto.LineLoginReq;

public interface AuthService {

    AuthResponseDTO handleFirebaseLogin(FirebaseLoginReq req) throws FirebaseAuthException;

    AuthResponseDTO handleLineLogin(LineLoginReq req) throws FirebaseAuthException;

    void logout();
}

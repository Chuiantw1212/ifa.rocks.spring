package rocks.ifa.spring.domain.auth;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.domain.auth.dtos.FirebaseLoginReq;
import rocks.ifa.spring.domain.auth.dtos.LineLoginReq;
import rocks.ifa.spring.domain.agent.dtos.AuthResponse;

public interface AuthService {

    AuthResponse handleFirebaseLogin(FirebaseLoginReq req) throws FirebaseAuthException;

    AuthResponse handleLineLogin(LineLoginReq req) throws FirebaseAuthException;

    void logout();
}

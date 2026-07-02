package rocks.ifa.spring.auth;

import rocks.ifa.spring.auth.dtos.AuthRes;
import rocks.ifa.spring.auth.dtos.LoginReq;
import rocks.ifa.spring.auth.dtos.FirebaseCustomToken;
import rocks.ifa.spring.auth.dtos.LiffIdToken;

public interface AuthService {
    AuthRes loginWithFirebase(LoginReq req);
    FirebaseCustomToken loginWithLiff(LiffIdToken idToken);
    void logout();
}

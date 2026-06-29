package rocks.ifa.spring.domain.agent;

import rocks.ifa.spring.domain.agent.dtos.AuthRes;
import rocks.ifa.spring.domain.agent.dtos.LiffLoginReq;

public interface LiffAuthService {
    /**
     * Handles the LINE LIFF login process.
     * @param req The request containing the LIFF ID token.
     * @return An authentication response containing a Firebase custom token.
     */
    AuthRes loginWithLiff(LiffLoginReq req);
}

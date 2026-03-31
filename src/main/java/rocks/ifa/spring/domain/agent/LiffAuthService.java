package rocks.ifa.spring.domain.agent;

import rocks.ifa.spring.domain.agent.contracts.AuthRes;
import rocks.ifa.spring.domain.agent.contracts.LiffLoginReq;

public interface LiffAuthService {
    /**
     * Handles the LINE LIFF login process.
     * @param req The request containing the LIFF ID token.
     * @return An authentication response containing a Firebase custom token.
     */
    AuthRes loginWithLiff(LiffLoginReq req);
}

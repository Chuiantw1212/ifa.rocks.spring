package rocks.ifa.spring.domain.agent;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.domain.agent.dtos.AuthRes;
import rocks.ifa.spring.domain.agent.dtos.LiffLoginReq;

/*
  This entire implementation is temporarily commented out to isolate build issues.
  It will be restored after the main application can successfully build and deploy.
*/
@Service
public class LiffAuthServiceImpl implements LiffAuthService {

    @Override
    public AuthRes loginWithLiff(LiffLoginReq req) {
        // Temporarily return null or throw an exception, as this path is disabled.
        throw new UnsupportedOperationException("LIFF login is temporarily disabled.");
    }
}

package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.UserRecord;
import rocks.ifa.spring.domain.agent.contracts.AuthRes;

public interface AuthService {
    AuthRes handlePostLogin(UserRecord userRecord);
}

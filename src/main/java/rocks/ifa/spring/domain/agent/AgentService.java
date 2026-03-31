package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.domain.agent.contracts.*;

public interface AgentService {

    // === Authentication ===
    AuthRes login(LoginReq req);
    void logout(String agentId);

    // === CRUD Operations ===
    AgentRes createAgent(CreateAgentReq req) throws FirebaseAuthException;
    AgentRes getAgent(String agentId) throws FirebaseAuthException;
    AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent() throws FirebaseAuthException;
}

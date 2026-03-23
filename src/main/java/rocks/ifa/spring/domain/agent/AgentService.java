package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;

public interface AgentService {

    // === Authentication ===
    AgentContracts.AuthRes login(AgentContracts.LoginReq req);
    void logout(String agentId);

    // === CRUD Operations ===
    AgentContracts.AgentRes createAgent(AgentContracts.CreateAgentReq req) throws FirebaseAuthException;
    AgentContracts.AgentRes getAgent(String agentId) throws FirebaseAuthException;
    AgentContracts.AgentRes updateAgent(String agentId, AgentContracts.UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent(String agentId) throws FirebaseAuthException;
}

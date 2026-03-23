package rocks.ifa.spring.domain.agent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.google.firebase.auth.FirebaseAuthException;

public interface AgentService {

    // === Authentication ===
    AgentContracts.AuthRes login(AgentContracts.LoginReq req);
    void logout(String agentId);

    // === CRUD Operations ===
    AgentContracts.AgentRes createAgent(AgentContracts.CreateAgentReq req) throws FirebaseAuthException;
    AgentContracts.AgentRes getAgent(String agentId) throws FirebaseAuthException;
    Page<AgentContracts.AgentRes> listAgents(Pageable pageable);
    AgentContracts.AgentRes updateAgent(String agentId, AgentContracts.UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent(String agentId) throws FirebaseAuthException;
}

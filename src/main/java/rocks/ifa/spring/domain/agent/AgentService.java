package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import rocks.ifa.spring.domain.agent.dtos.*;

public interface AgentService {

    // === CRUD Operations ===
    AgentRes createAgent(CreateAgentReq req) throws FirebaseAuthException;
    AgentRes getAgent(String agentId);
    AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent() throws FirebaseAuthException;
    UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture);
}

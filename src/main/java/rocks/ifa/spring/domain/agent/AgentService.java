package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import rocks.ifa.spring.domain.agent.dtos.*;
import rocks.ifa.spring.domain.line.LineTokenPayload;

public interface AgentService {

    // loginWithLine has been moved to AuthService

    // === Account Binding Operation ===
    AgentRes bindLineUserToAgent(LineTokenPayload lineTokenPayload);

    // === Original CRUD Operations ===
    AgentRes createAgent(CreateAgentReq req) throws FirebaseAuthException;
    AgentRes getAgent(String agentId);
    AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent() throws FirebaseAuthException;
    UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture);
}

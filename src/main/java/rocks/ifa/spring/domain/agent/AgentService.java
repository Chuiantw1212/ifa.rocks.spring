package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import rocks.ifa.spring.domain.agent.dtos.*;
import rocks.ifa.spring.domain.line.LineTokenPayload;

public interface AgentService {

    AgentRes bindLineUserToAgent(LineTokenPayload lineTokenPayload);

    AgentRes getAgent(String agentId); // This finds by internal UUID
    AgentRes getAgentByFirebaseUid(String firebaseUid); // This finds by Firebase UID

    AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent() throws FirebaseAuthException;
    UserRecord findOrCreateAgentByLineId(String lineUserId, String name, String picture);
}

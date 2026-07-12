package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import rocks.ifa.spring.domain.agent.dtos.AgentRecord;
import rocks.ifa.spring.domain.agent.dtos.UpdateAgentReq;
import rocks.ifa.spring.domain.line.LineTokenPayload;

public interface AgentService {
    AgentRecord getAgentByFirebaseUid(String firebaseUid);
    AgentRecord bindLineUserToAgent(LineTokenPayload lineTokenPayload);
    AgentRecord updateAgent(String id, UpdateAgentReq req) throws FirebaseAuthException;
    void deleteAgent() throws FirebaseAuthException;
}

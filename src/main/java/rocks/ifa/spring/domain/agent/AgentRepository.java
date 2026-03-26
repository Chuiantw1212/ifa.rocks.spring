package rocks.ifa.spring.domain.agent;

import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rocks.ifa.spring.domain.agent.contracts.*;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, String> {
    interface AgentService {

        // === Authentication ===
        AuthRes login(LoginReq req);
        void logout(String agentId);

        // === CRUD Operations ===
        AgentRes createAgent(CreateAgentReq req) throws FirebaseAuthException;
        AgentRes getAgent(String agentId) throws FirebaseAuthException;
        AgentRes updateAgent(String agentId, UpdateAgentReq req) throws FirebaseAuthException;
        void deleteAgent(String agentId) throws FirebaseAuthException;
    }
}

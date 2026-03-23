package rocks.ifa.spring.domain.agent;

public interface AgentService {
    void registerAgent(AgentRegistrationReq req);
    void deleteAgent(String uid);
}

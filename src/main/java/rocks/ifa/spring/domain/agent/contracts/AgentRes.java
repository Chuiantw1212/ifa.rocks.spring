package rocks.ifa.spring.domain.agent.contracts;

public record AgentRes(
    String uid,
    String email,
    String displayName,
    boolean disabled
) {}

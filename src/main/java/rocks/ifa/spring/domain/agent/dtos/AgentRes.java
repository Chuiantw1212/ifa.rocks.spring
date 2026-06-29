package rocks.ifa.spring.domain.agent.dtos;

public record AgentRes(
    String uid,
    String email,
    String displayName,
    boolean disabled
) {}

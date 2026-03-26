package rocks.ifa.spring.domain.agent.contracts.dto;

public record AgentRes(
    String uid,
    String email,
    String displayName,
    boolean disabled
) {}

package rocks.ifa.spring.domain.agent.contracts;

public record AuthRes(
    String token,
    AgentRes agent
) {}

package rocks.ifa.spring.domain.agent.dtos;

public record AuthRes(
    String token,
    AgentRes agent
) {}

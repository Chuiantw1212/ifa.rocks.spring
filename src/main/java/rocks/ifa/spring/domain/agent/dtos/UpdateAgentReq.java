package rocks.ifa.spring.domain.agent.dtos;

public record UpdateAgentReq(
    String displayName,
    boolean disabled
) {}

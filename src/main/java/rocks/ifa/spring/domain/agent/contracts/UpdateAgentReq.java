package rocks.ifa.spring.domain.agent.contracts.dto;

public record UpdateAgentReq(
    String displayName,
    boolean disabled
) {}
